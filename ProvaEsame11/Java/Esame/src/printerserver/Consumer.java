package printerserver;

import java.util.Hashtable;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

import buffer.SharedQueue;

public class Consumer implements Runnable {
    private final SharedQueue queue; 

    public Consumer(SharedQueue queue){
        this.queue= queue;
    }

    @Override
    public void run(){
            Hashtable<String,String>env = new Hashtable<>(); 
            env.put(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory" );
            env.put(Context.PROVIDER_URL, "tcp://127.0.0.1:61616"); 
            env.put("queue.bw","bw"); 
            env.put("queue.color","color"); 
            try{
                Context ctx = new InitialContext(env); 
                QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory"); 
                QueueConnection qconn = qcf.createQueueConnection(); 
                qconn.start(); 

                QueueSession qsession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); 
                QueueSender bwSender = qsession.createSender((Queue) ctx.lookup("bw")); 
                QueueSender colorSender = qsession.createSender((Queue) ctx.lookup("color"));

                while(true){
                    String msg = queue.take(); 
                    TextMessage tmsg = qsession.createTextMessage(msg);

                    if(msg.contains("color")){
                        colorSender.send(tmsg); 
                    }else{
                        bwSender.send(tmsg); 
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
    }
    
}
