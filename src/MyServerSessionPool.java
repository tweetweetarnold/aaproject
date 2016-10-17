import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSession;
import javax.jms.ServerSessionPool;
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
}