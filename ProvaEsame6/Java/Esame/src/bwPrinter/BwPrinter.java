package bwPrinter;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;

public class BwPrinter implements MessageListener {

    private final String filtro;

    public BwPrinter(String filtro) {
        this.filtro = filtro.toLowerCase();
    }

    public static void main(String[] args) {
        if (args.length != 1 || (!args[0].equals("bw") && !args[0].equals("gs"))) {
            System.out.println("Uso: java BWPrinter <bw|gs>");
            return;
        }

        try {
            String filtro = args[0];
            BwPrinter printer = new BwPrinter(filtro);

            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.put(Context.PROVIDER_URL, "tcp://127.0.0.1:61616");
            env.put("queue.bw", "bw");

            Context ctx = new InitialContext(env);
            QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            Queue queue = (Queue) ctx.lookup("bw");

            QueueConnection conn = factory.createQueueConnection();
            conn.start();
            QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueReceiver receiver = session.createReceiver(queue);

            receiver.setMessageListener(printer);
            System.out.println("[BWPrinter] In ascolto su /queue/bw per filtro: " + filtro);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message msg) {
        try {
            if (msg instanceof TextMessage) {
                String body = ((TextMessage) msg).getText();
                System.out.println("[BWPrinter] Ricevuto: " + body);
                if (body.contains(filtro)) {
                    try (PrintWriter out = new PrintWriter(new FileWriter("bw.txt", true))) {
                        out.println(body);
                    }
                    System.out.println("[BWPrinter] Scritto su bw.txt");
                } else {
                    System.out.println("[BWPrinter] Messaggio ignorato (non contiene '" + filtro + "')");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
