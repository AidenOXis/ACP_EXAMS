package infofilter;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;


public class InfoFilter {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Uso: java ErrorChecker <parolaChiave>");
            return;
        }

        String keyword = args[0];

        Hashtable<String, String> props = new Hashtable<>();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.put(Context.PROVIDER_URL, "tcp://127.0.0.1:61616");
        props.put("queue.info", "info");

        Context ctx = new InitialContext(props);
        QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
        QueueConnection connection = factory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) ctx.lookup("info");
        QueueReceiver receiver = session.createReceiver(queue);

        receiver.setMessageListener(new Info_Listener(keyword));
        connection.start();

        System.out.println("[ErrorChecker] In ascolto su 'error' con filtro: " + keyword);
    }
}
