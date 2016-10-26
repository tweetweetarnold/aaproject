import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ServerSession;
import javax.jms.ServerSessionPool;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;

public class MyServerSessionPool implements ServerSessionPool, ExceptionListener {
	private final TopicConnection _conn;

	MyServerSessionPool(TopicConnection conn) {
		_conn = conn;
	}

	public ServerSession getServerSession() {
		return new MyServerSession(_conn);
	}

	public void onException(JMSException ex) {
		ex.printStackTrace();
	}

//	@Override
//	public void onMessage(Message msg) {
//		// TODO Auto-generated method stub
//		TextMessage m = (TextMessage) msg;
//		try {
//			System.out.println("thread" + Thread.currentThread().getName() + ": " + m.getText());
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}