
import javax.jms.DeliveryMode;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

public class Publisher {
	public static void main(String[] args) throws Exception {
		// get the initial context
		InitialContext ctx = new InitialContext();

		// lookup the topic object
		Topic topic = (Topic) ctx.lookup("jms/topic0");

		// lookup the topic connection factory
		TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup("jms/__defaultConnectionFactory");
		System.out.println("ok");

		// create a topic connection
		TopicConnection topicConn = connFactory.createTopicConnection();
		System.out.println("clientID: " + topicConn.getClientID());
		// TopicConnection topicConn =
		// connFactory.createTopicConnection("publisher1", "123");
		System.out.println("still ok");

		// create a topic session
		TopicSession topicSession = topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

		// create a topic publisher
		TopicPublisher topicPublisher = topicSession.createPublisher(topic);
//		topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		topicPublisher.setDeliveryMode(DeliveryMode.PERSISTENT);

		// create the "Hello World" message
		TextMessage message = topicSession.createTextMessage();
		message.setText("Hello World");

		// publish the messages
		topicPublisher.publish(message);

		// print what we did
		System.out.println("published: " + message.getText());

		// close the topic connection
		topicConn.close();
	}
}