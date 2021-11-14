package Domain;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SensorPublisher {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static ActiveMQConnectionFactory connectionFactory;
    private static Connection connection;
    private static Session session;
    private static Destination destination;
    private static TextMessage textMessage;
    private static MessageProducer messageProducer;
    private static String topic;

    public SensorPublisher() throws JMSException {
        createConnection();
        startConnection();
        createSession();
    }

    public void createTopic(String withName) throws JMSException {
        this.topic = withName;
        this.destination = this.session.createTopic(withName);
        createPublisher();
    }

    public String getTopic() {
        return this.topic;
    }

    public void createMessage(String message) throws JMSException {
        textMessage = this.session.createTextMessage();
        textMessage.setText(message);

        messageProducer.send(textMessage);

//        messageProducer.close();
//        session.close();
//        connection.close();
    }

    // MARK: Private Methods

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

    private void createPublisher() throws JMSException {
        this.messageProducer = session.createProducer(this.destination);
    }
}
