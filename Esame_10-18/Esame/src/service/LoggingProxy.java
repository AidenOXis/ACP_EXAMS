package service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import logging.ILogging;

public class LoggingProxy implements ILogging {

    private String addr;
    private int port; 

    public LoggingProxy(String a, int p){
        addr = new String(a); 
        port = p; 

    
    
    
    
    }
    @Override
    public void log(String msg, int type){
       try{
        Socket s = new Socket(addr,port); 
        DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
        DataInputStream dataIn = new DataInputStream(new BufferedInputStream(s.getInputStream()));

        dataOut.writeUTF(msg);
        dataOut.writeInt(type); 
        dataOut.flush(); 
        
   

        dataIn.close(); 
        dataOut.close(); 
        s.close(); 
    }catch (UnknownHostException u){
        u.printStackTrace();
    }catch (IOException e){
        e.printStackTrace();
    }
    }
}
