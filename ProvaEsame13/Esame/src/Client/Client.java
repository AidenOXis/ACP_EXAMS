package Client;

import java.util.Hashtable;
import java.util.Random;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Client {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Uso: java Client <type> (buy o stats)");
            return;
        }

        String requestType = args[0];
        String[] artists = {"Jovanotti", "Ligabue", "Negramaro"};
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, "tcp://127.0.0.1:61616");
        env.put("topic.request", "request");

        Context ctx = new InitialContext(env);
        TopicConnectionFactory fconn = (TopicConnectionFactory) ctx.lookup("TopicConnectionFactory");
        TopicConnection tcon = fconn.createTopicConnection();
        TopicSession tses = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = (Topic) ctx.lookup("request");
        TopicPublisher publisher = tses.createPublisher(topic);

        tcon.start();

        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            MapMessage message = tses.createMapMessage();
            message.setString("type", requestType);
            if (requestType.equals("buy")) {
                String artist = artists[rand.nextInt(artists.length)];
                message.setString("value", artist);
                System.out.println("[Client] Messaggio inviato: buy " + artist);
            } else if (requestType.equals("stats")) {
                message.setString("value", "Sold");
                System.out.println("[Client] Messaggio inviato: stats Sold");
            } else {
                System.out.println("Tipo richiesta non valido.");
                break;
            }
            publisher.publish(message);
            Thread.sleep(2000);
        }

        tcon.close();
        tses.close();
    }
}