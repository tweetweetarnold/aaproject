
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

public class MyReceiver implements MessageListener {

	public static void main(String[] args) throws JMSException, NamingException, IOException {

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		String username = "MyReceiver";
		System.out.println("Enter for : " + username);
		System.out.print("Listening from Address: ");
		String fromAddress = bufferedReader.readLine();
		// System.out.print("Receiver Address: ");
		// String receiver = bufferedReader.readLine();

		System.out.println("****************************");
		System.out.println("Username: " + username);
		System.out.println("Listening from: " + fromAddress);
		System.out.println("****************************");

		MyReceiver commandLineChat = new MyReceiver();
		Context initialContext = MyReceiver.getInitialContext();
		Queue q1 = (Queue) initialContext.lookup(fromAddress);
		// Queue q2 = (Queue) initialContext.lookup(receiver);

		JMSContext jmsContext = ((ConnectionFactory) initialContext.lookup("java:comp/DefaultJMSConnectionFactory"))
				.createContext();
		jmsContext.createConsumer(q1).setMessageListener(commandLineChat);

		// JMSProducer jmsProducer = jmsContext.createProducer();

		String messageToSend = null;

		System.out.println("username " + username + " is waiting from " + fromAddress + " ...");
		while (true) {
			messageToSend = bufferedReader.readLine();
			if (messageToSend.equalsIgnoreCase("exit")) {
				jmsContext.close();
				System.exit(0);
			} else {
				System.out.println("Error! Please type 'exit' to quit");
			}
		}

	}

	// public static ArrayList<String> getMessages() {
	//
	// ArrayList<String> list = new ArrayList<>();
	// for (int i = 0; i < 1000; i++) {
	// list.add("Line" + (i + 1));
	// }
	// return list;
	// }

	public static Context getInitialContext() throws JMSException, NamingException {
		Properties p = new Properties();
		p.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
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
