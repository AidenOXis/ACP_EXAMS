package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import logging.ILogging;

public abstract class LoggingSkeleton implements ILogging {
    private int port; 

    public LoggingSkeleton(int p){
        this.port = p; 
    }
    @Override
    public abstract void log(String message,int type); 

    public void runSkeleton(){
        try{
            ServerSocket server = new ServerSocket(port); 
            while (true){
                Socket s = server.accept(); 
                Thread t = new ServerThread(s,this); 
                t.start(); 
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
