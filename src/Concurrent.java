import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

public class Concurrent {
	public static void main(String[] args) throws Exception {
		// get the initial context
		InitialContext ctx = new InitialContext();

		// lookup the topic object
		Topic topic = (Topic) ctx.lookup("jms/topic0");

		// lookup the topic connection factory
		TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx
				.lookup("topic/connectionFactory2");

		// create a topic connection
		TopicConnection topicConn = connFactory.createTopicConnection();

		// create a server session pool
		MyServerSessionPool ssPool = new MyServerSessionPool(topicConn);
		topicConn.setExceptionListener(ssPool);

		// ****ERROR HERE****
		// create a topic connection consumer
		// ConnectionConsumer connConsumer =
		// topicConn.createConnectionConsumer(topic, null, ssPool, 10);

		// start the connection
		topicConn.start();

		// send some messages to the newly created connection consumer
		int ackMode = Session.AUTO_ACKNOWLEDGE;
		TopicSession session = topicConn.createTopicSession(false, ackMode);
		TopicPublisher publisher = session.createPublisher(topic);

		TextMessage msg = session.createTextMessage();
		int num = 100000;

		for (int i = 0; i < num; i++) {
			msg.setText("This is message " + i);
			publisher.publish(msg);
		}

		System.out.println("sent " + num + " messages");

		publisher.close();

		// wait for connection consumer
		//		while (true) {
		//			Thread.sleep(10000);
		//		}
	}
}