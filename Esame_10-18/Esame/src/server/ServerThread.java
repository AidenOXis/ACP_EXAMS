package server;

import java.io.BufferedInputStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import logging.ILogging;

public class ServerThread extends Thread{
    private Socket socket; 
    private ILogging impl; 
    
    public ServerThread(Socket s, ILogging impl){
        this.socket = s;
        this.impl = impl; 
    }
    public void run(){
        try{
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String msg = in.readUTF(); 
            int type = in.readInt(); 

            impl.log(msg,type); 
            socket.close(); 
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
