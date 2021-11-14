package View;

import DomainInterface.ConsumerUIInterfacing;
import Domain.SensorSubscriber;

import javax.jms.JMSException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsumerUI extends JFrame implements ActionListener, ConsumerUIInterfacing {
    private JCheckBox tempCheckBox;
    private JCheckBox umdCheckBox;
    private JCheckBox velocCheckBox;
    private JTextArea logTextArea;
    private JPanel mainPanel;
    private JButton clearButton;
    private static SensorSubscriber sensorSubscriber;

    public ConsumerUI() throws JMSException {
        setupUI();
        setupActionListenerToComponents();
        sensorSubscriber = new SensorSubscriber(this);
    }

    public void setLog(String message) {
        logTextArea.append(message + "\n");
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getActionCommand();
        Object obj = e.getSource();
        Boolean checkBoxSelected = isCheckBoxSelected(obj);
        String sourceString = source.toString();

        switch (sourceString) {
            case "Temperatura":
                try {
                    printLog(checkBoxSelected, sourceString);
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
                try {
                    sensorSubscriber.createTopic("Temperatura");
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Umidade":
                try {
                    printLog(checkBoxSelected, sourceString);
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
                try {
                    sensorSubscriber.createTopic("Umidade");
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Velocidade":
                try {
                    printLog(checkBoxSelected, sourceString);
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
                try {
                    sensorSubscriber.createTopic("Velocidade");
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
                break;
            case "clearBtn":
                logTextArea.setText(null);
                System.out.println("limpar");
                break;
        }
    }

    public static void main (String[]args) throws JMSException {
        ConsumerUI consumerUI = new ConsumerUI();
    }

    // MARK: Private Methods

    private void setupUI () {
        setTitle("Consumer iSensor");
        setContentPane(mainPanel);
        setSize(500, 500);
        setVisible(true);
        setResizable(false);
        setLayout(null);
        logTextArea.setEditable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setupActionListenerToComponents () {
        tempCheckBox.addActionListener(this);
        tempCheckBox.setActionCommand("Temperatura");

        umdCheckBox.addActionListener(this);
        umdCheckBox.setActionCommand("Umidade");

        velocCheckBox.addActionListener(this);
        velocCheckBox.setActionCommand("Velocidade");

        clearButton.addActionListener(this);
        clearButton.setActionCommand("clearBtn");
    }

    private void printLog(Boolean selected, String from) throws JMSException {
        if (selected) {
            setLog("[Log] ⚠️ Inscrito para monitorar o sensor de "+ from +"...");
        } else {
            setLog("[Log] ⚠️ Desinscrito para monitorar o sensor de "+ from + "...");
        }
    }

    private Boolean isCheckBoxSelected(Object obj) {
        if (obj instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) obj;
            return cb.isSelected();
        }

        return false;
    }
}
