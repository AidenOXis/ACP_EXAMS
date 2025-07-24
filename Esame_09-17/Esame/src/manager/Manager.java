package Manager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Manager {
    public static void main(String[] args) throws JMSException, NamingException, IOException {
        // === CONFIGURAZIONE ENV PER ActiveMQ ===
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, "tcp://127.0.0.1:61616");

        // definizione alias per JNDI
        env.put("topic.request", "request");
        env.put("topic.stats", "stats");
        env.put("topic.tickets", "tickets");

        // === CONNESSIONE ===
        Context ctx = new InitialContext(env);
        TopicConnectionFactory factory = (TopicConnectionFactory) ctx.lookup("TopicConnectionFactory");
        TopicConnection tcon = factory.createTopicConnection();
        tcon.start();

        TopicSession tses = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        // === LOOKUP TOPIC ===
        Topic requestTopic = (Topic) ctx.lookup("request");   
        Topic statsTopic = (Topic) ctx.lookup("stats");
        Topic ticketsTopic = (Topic) ctx.lookup("tickets");

        // === SUBSCRIBER SU requestTopic ===
        TopicSubscriber subscriber = tses.createSubscriber(requestTopic);
        subscriber.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof MapMessage) {
                        MapMessage map = (MapMessage) message;
                        String type = map.getString("type");
                        String value = map.getString("value");

                        // Se è "stats", pubblica su statsTopic
                        if ("stats".equals(type)) {
                            TextMessage textMessage = tses.createTextMessage(value);
                            TopicPublisher publisher = tses.createPublisher(statsTopic);
                            publisher.publish(textMessage);
                            System.out.println("[Manager] Pubblicato su stats: " + value);
                        }
                        // Se è "buy", salva su file
                        else if ("buy".equals(type)) {
                            try (FileWriter writer = new FileWriter("tickets.txt", true)) {
                                writer.write(value + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("[Manager] Ordine salvato: " + value);
                        }

                        // In ogni caso, pubblica anche su tickets
                        TextMessage textMessage = tses.createTextMessage(value);
                        TopicPublisher publisher = tses.createPublisher(ticketsTopic);
                        publisher.publish(textMessage);
                        System.out.println("[Manager] Pubblicato su tickets: " + value);
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });



        // === CHIUSURA RISORSE ===
        tses.close();
        tcon.close();
    }
}
