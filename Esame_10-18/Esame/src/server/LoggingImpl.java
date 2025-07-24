package server;

import java.util.Hashtable;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class LoggingImpl extends LoggingSkeleton {


    private QueueConnection queueConnection;
    private QueueSession queueSession;
    private Queue infoQueue;
    private Queue errorQueue;
    private QueueSender infoSender;
    private QueueSender errorSender;
    public LoggingImpl(int port){
        super(port); 

        
            Hashtable<String, String> props = new Hashtable<>();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.put(Context.PROVIDER_URL, "tcp://127.0.0.1:61616");
            props.put("queue.info", "info");
            props.put("queue.error", "error");
            try{
            Context ctx = new InitialContext(props);
            QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
            queueConnection = factory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            infoQueue = (Queue) ctx.lookup("info");
            errorQueue = (Queue) ctx.lookup("error");
            infoSender = queueSession.createSender(infoQueue);
            errorSender = queueSession.createSender(errorQueue);

            queueConnection.start();
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        @Override
        public synchronized void log(String msg,int type){
            System.out.println("[LoggingImpl] Ricevuto: " + msg + " - tipo " + type);
       Thread t = new Thread(() -> {
            try {
                MapMessage message = queueSession.createMapMessage();
                message.setString("msg", msg);
                message.setInt("type", type);

                // Sincronizzazione solo su invio messaggio JMS<
                synchronized (this) {
                    if (type == 2) {
                        errorSender.send(message);
                        System.out.println("→ Inviato su queue: error");
                    } else {
                        infoSender.send(message);
                        System.out.println("→ Inviato su queue: info");
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
            t.start(); 
        }
    }
    

