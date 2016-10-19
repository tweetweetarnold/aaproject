import javax.naming.InitialContext;
                                                                           
import javax.jms.Topic;
import javax.jms.Session;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import javax.jms.JMSException;
import javax.jms.ExceptionListener;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
                                                                           
public class AsyncSubscriber implements MessageListener, ExceptionListener
{
    public static void main(String[] args) throws Exception
    {
       // get the initial context
       InitialContext ctx = new InitialContext();
                                                                          
       // lookup the topic object
       Topic topic = (Topic) ctx.lookup("topic/topic0");
                                                                          
       // lookup the topic connection factory
       TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.
           lookup("topic/connectionFactory");
                                                                          
       // create a topic connection
       TopicConnection topicConn = connFactory.createTopicConnection();
                                                                          
       // create a topic session
       TopicSession topicSession = topicConn.createTopicSession(false,
           Session.AUTO_ACKNOWLEDGE);
                                                                          
       // create a topic subscriber
       TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
                                                                          
       // set an asynchronous message listener
       AsyncSubscriber asyncSubscriber = new AsyncSubscriber();
       topicSubscriber.setMessageListener(asyncSubscriber);
                                                                          
       // set an asynchronous exception listener on the connection
       topicConn.setExceptionListener(asyncSubscriber);
                                                                          
       // start the connection
       topicConn.start();
                                                                          
       // wait for messages
       System.out.print("waiting for messages");
       for (int i = 0; i < 10; i++) {
          Thread.sleep(1000);
          System.out.print(".");
       }
       System.out.println();
                                                                          
       // close the topic connection
       topicConn.close();
    }
                                                                           
    /**
       This method is called asynchronously by JMS when a message arrives
       at the topic. Client applications must not throw any exceptions in
       the onMessage method.
       @param message A JMS message.
     */
    public void onMessage(Message message)
    {
       TextMessage msg = (TextMessage) message;
       try {
          System.out.println("received: " + msg.getText());
       } catch (JMSException ex) {
          ex.printStackTrace();
       }
    }
                                                                           
    /**
       This method is called asynchronously by JMS when some error occurs.
       When using an asynchronous message listener it is recommended to use
       an exception listener also since JMS have no way to report errors
       otherwise.
       @param exception A JMS exception.
     */
    public void onException(JMSException exception)
    {
       System.err.println("something bad happended: " + exception);
    }
}