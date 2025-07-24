package service;

import java.util.Random;

public class ServiceThread extends Thread{

    private String ip; 
    private int port; 
    private static final String[] debugInfoMessages = {"success","checking"};
    private static final String[] errorMessages = {"fatal","exception"};

    public ServiceThread(String ip, int port){
        this.ip = ip; 
        this.port = port; 
    }

    public void run(){
        Random rand = new Random(); 
        int type = rand.nextInt(3);
        String msg; 


        if (type == 2){
            msg = errorMessages[rand.nextInt(errorMessages.length)];
        }else{
            msg = debugInfoMessages[rand.nextInt(debugInfoMessages.length)];
        }

        LoggingProxy proxy = new LoggingProxy(ip, port);
        proxy.log(msg,type);

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
}
