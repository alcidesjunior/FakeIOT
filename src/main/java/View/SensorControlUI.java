package View;

import DataSource.TemperaturaDataSource;
import DataSource.UmidadeDataSource;
import DataSource.VelocidadeDataSource;
import Domain.SensorPublisher;

import javax.jms.JMSException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class SensorControlUI extends JFrame implements ActionListener {
    private JRadioButton tempRadioButton;
    private JRadioButton umidadeRadioButton;
    private JRadioButton velocidadeRadioButton;
    private JTextField minTextField;
    private JTextField maxTextField;
    private JButton salvarButton;
    private JPanel mainPanel;
    private String currentRadio = "Temperatura";
    private static TemperaturaDataSource temperaturaDataSource;
    private static UmidadeDataSource umidadeDataSource;
    private static VelocidadeDataSource velocidadeDataSource;
    private SensorPublisher sensorPublisher;

    public SensorControlUI() throws JMSException {
        setupUI();
        setupActionListenerToRadioButtons();
        handleSaveButton();

    }

    public static void main(String[] args) throws JMSException {
        SensorControlUI sensorControlUI = new SensorControlUI();
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getActionCommand();

       switch (source.toString()) {
           case "Temperatura":
               currentRadio = "Temperatura";
               System.out.println("TEMPERATURA");

               if(temperaturaDataSource.getInstance() != null) {
                   minTextField.setText(temperaturaDataSource.getInstance().getMin());
                   maxTextField.setText(temperaturaDataSource.getInstance().getMax());
               }

               break;
           case "Umidade":
               currentRadio = "Umidade";
               System.out.println("UMIDADE");

               if(umidadeDataSource.getInstance() != null) {
                   minTextField.setText(umidadeDataSource.getInstance().getMin());
                   maxTextField.setText(umidadeDataSource.getInstance().getMax());
               }
               break;
           case "Velocidade":
               currentRadio = "Velocidade";
               System.out.println("VELOCIDADE");

               if(velocidadeDataSource.getInstance() != null) {
                   minTextField.setText(velocidadeDataSource.getInstance().getMin());
                   maxTextField.setText(velocidadeDataSource.getInstance().getMax());
               }

               break;
           case "saveBtn":
               if(currentRadio.equals("Temperatura")) {
                   System.out.println("aqui");

                   if(temperaturaDataSource.getInstance() != null) {
                       temperaturaDataSource.getInstance().setMax(maxTextField.getText());
                       temperaturaDataSource.getInstance().setMin(minTextField.getText());
                   }

                   try {
                       sensorPublisher = new SensorPublisher();
                       sensorPublisher.createTopic("Temperatura");
                       int min = Integer.parseInt(temperaturaDataSource.getInstance().getMin());
                       int max = Integer.parseInt(temperaturaDataSource.getInstance().getMax());
                       fakeSensor(min, max, sensorPublisher);
                   } catch (JMSException ex) {
                       ex.printStackTrace();
                   }

               } else if(currentRadio.equals("Umidade")) {
                   if(umidadeDataSource.getInstance() != null) {
                       umidadeDataSource.getInstance().setMax(maxTextField.getText());
                       umidadeDataSource.getInstance().setMin(minTextField.getText());
                   }

                   try {
                       sensorPublisher = new SensorPublisher();
                       sensorPublisher.createTopic("Umidade");
                       int min = Integer.parseInt(umidadeDataSource.getInstance().getMin());
                       int max = Integer.parseInt(umidadeDataSource.getInstance().getMax());
                       fakeSensor(min, max, sensorPublisher);
                   } catch (JMSException ex) {
                       ex.printStackTrace();
                   }
               } else if(currentRadio.equals("Velocidade")) {
                   if(velocidadeDataSource.getInstance() != null) {
                       velocidadeDataSource.getInstance().setMax(maxTextField.getText());
                       velocidadeDataSource.getInstance().setMin(minTextField.getText());
                   }

                   try {
                       sensorPublisher = new SensorPublisher();
                       sensorPublisher.createTopic("Velocidade");
                       int min = Integer.parseInt(velocidadeDataSource.getInstance().getMin());
                       int max = Integer.parseInt(velocidadeDataSource.getInstance().getMax());
                       fakeSensor(min, max, sensorPublisher);
//                       SensorSimulator sensorSimulator = new SensorSimulator(min, max, sensorPublisher);
//
//
//                       Timer timer = new Timer();
//                       timer.schedule(sensorSimulator, 0, 1000);
                   } catch (JMSException ex) {
                       ex.printStackTrace();
                   }
               }

                // SIMULADOR


               System.out.println("Setando min="+minTextField.getText()+ " e max="+maxTextField.getText()+" para "+ currentRadio);
               break;
       }
    }

    // MARK: Private methods

    private void setupActionListenerToRadioButtons() {
        tempRadioButton.setSelected(true);
        tempRadioButton.addActionListener(this);
        tempRadioButton.setActionCommand("Temperatura");

        umidadeRadioButton.addActionListener( this);
        umidadeRadioButton.setActionCommand("Umidade");

        velocidadeRadioButton.addActionListener(this);
        velocidadeRadioButton.setActionCommand("Velocidade");
    }

    private void handleSaveButton() {
        salvarButton.addActionListener(this);
        salvarButton.setActionCommand("saveBtn");
    }

    private void setupUI() {
        setTitle("Control Panel iSensor");
        setContentPane(mainPanel);
        setSize(400, 150);
        setVisible(true);
        setResizable(false);
        setLayout(null);
        minTextField.setText("0");
        maxTextField.setText("0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void fakeSensor(int min, int max, SensorPublisher sensorPublisher) {
        int currentNumber = getRandomNumber(min, max+1);
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        String unidade = "";

        if(sensorPublisher.getTopic().equals("Temperatura")) {
            unidade = "ºC";
        } else if(sensorPublisher.getTopic().equals("Umidade")) {
            unidade = "g/Kg";
        } else {
            unidade = "KM/H";
        }

        if(currentNumber >= max) {
            System.out.println("ALERTA Máximo !!!!"+ sensorPublisher.getTopic());
            try {
                sensorPublisher.createMessage("["+timeStamp+"] ALERTA DE "+sensorPublisher.getTopic()+" NO MAXIMO ["+currentNumber + unidade +"]!!!");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else if(currentNumber <= min) {
            System.out.println("ALERTA Minimo!!!!"+ sensorPublisher.getTopic());
            try {
                sensorPublisher.createMessage("["+timeStamp+"] ALERTA DE "+sensorPublisher.getTopic()+" NO MINIMO ["+currentNumber + unidade +"]!!!");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

    }

    private int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
