import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

public class Subscriber {
	public static void main(String[] args) throws Exception {
		// get the initial context
		InitialContext ctx = new InitialContext();

		// lookup the topic object
		Topic topic = (Topic) ctx.lookup("jms/topic0");

		// lookup the topic connection factory
		TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup("topic/connectionFactory");

		// create a topic connection
		TopicConnection topicConn = connFactory.createTopicConnection();
		System.out.println("clientid: " + topicConn.getClientID());
		
		MyServerSessionPool ssPool = new MyServerSessionPool(topicConn);

		System.out.println("ok1");
		// create a topic session
		TopicSession topicSession = topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		topicConn.createConnectionConsumer(topic, null, ssPool, 10);
		System.out.println("ok2");

		// create a topic subscriber
		// TopicSubscriber topicSubscriber =
		// topicSession.createSubscriber(topic);
		TopicSubscriber topicSubscriber = topicSession.createDurableSubscriber(topic, "arnold");

		
//		topicSubscriber.setMessageListener(ssPool);

		// start the connection
		topicConn.start();
		System.out.println("started...");

		// receive the message
		// boolean isStarted = false;
		// long start = 0;
		//
		// while (true) {
		// Message message = topicSubscriber.receive();
		//
		// if (!isStarted) {
		// isStarted = true;
		// start = System.currentTimeMillis();
		// System.out.println("time start");
		// }
		//
		// TextMessage msg = ((TextMessage) message);
		// String a = msg.getText();
		// System.out.println("single-thread: " + a);
		//
		// if (a.equalsIgnoreCase("This is message 99999")) {
		// break;
		// }
		//
		// }
		// long end = System.currentTimeMillis();
		// System.out.println("Time taken: " + (end - start));
		// close the topic connection
		// topicConn.close();

		while (true) {
			Thread.sleep(10000);
		}
	}
}