package Domain;

import javax.jms.*;

import DomainInterface.ConsumerUIInterfacing;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SensorSubscriber implements MessageListener {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static ActiveMQConnectionFactory connectionFactory;
    private static Connection connection;
    private static Session session;
    private static Destination destination;
    private static TextMessage textMessage;
    private static MessageConsumer messageConsumer;
    private static ConsumerUIInterfacing consumerUI;

    public SensorSubscriber(ConsumerUIInterfacing consumerUI) throws JMSException {
        createConnection();
        startConnection();
        createSession();
        this.consumerUI = consumerUI;
    }

    public void createTopic(String withName) throws JMSException {
        this.destination = this.session.createTopic(withName);
        createConsumer();
    }

    // MARK: private methods

    private ActiveMQConnectionFactory createConnection() {
        return new ActiveMQConnectionFactory(url);
    }

    private void startConnection() throws JMSException {
        this.connectionFactory = createConnection();

        this.connection = connectionFactory.createConnection();
        connection.start();
    }

    private void createSession() throws JMSException {
        this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private void createConsumer() throws JMSException {
        this.messageConsumer = this.session.createConsumer(destination);
        this.messageConsumer.setMessageListener(this);
    }

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage){
            try{
                consumerUI.setLog(((TextMessage)message).getText());
                System.out.println( ((TextMessage)message).getText());
            }catch(Exception e){
                System.out.println(e.getMessage());
                e.getStackTrace();
            }
        }
    }
}
