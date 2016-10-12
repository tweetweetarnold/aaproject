import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CommandLineChat implements MessageListener {

	public static void main(String[] args) throws JMSException, NamingException, IOException {
		if (args.length != 3) {
			System.out.println("usage: username subscribe-to-queue-name publish-to-queue-name");
		} else {
			String username = args[0];
			System.out.println("usage: " + username + " | subscribe to queue: " + args[1]
					+ " | publish to queue: " + args[2]);

			CommandLineChat commandLineChat = new CommandLineChat();
			Context initialContext = CommandLineChat.getInitialContext();
			Queue q1 = (Queue) initialContext.lookup(args[1]);
			Queue q2 = (Queue) initialContext.lookup(args[2]);
			JMSContext jmsContext = ((ConnectionFactory) initialContext
					.lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
			jmsContext.createConsumer(q1).setMessageListener(commandLineChat);

			JMSProducer jmsProducer = jmsContext.createProducer();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String messageToSend = null;
			while (true) {
				messageToSend = bufferedReader.readLine();
				if (messageToSend.equalsIgnoreCase("exit")) {
					jmsContext.close();
					System.exit(0);
				}

			}
		}

	}

	public static Context getInitialContext() throws JMSException, NamingException {
		Properties p = new Properties();
		p.setProperty("java.naming.factory.initial",
				"com.sun.enterprise.naming.SerialInitContextFactory");
		p.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		p.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		return new InitialContext(p);
	}

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println(message.getBody(String.class));
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
