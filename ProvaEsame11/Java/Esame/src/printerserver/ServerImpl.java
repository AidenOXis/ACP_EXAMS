package printerserver;

import Iprinter.Service;
import buffer.SharedQueue;

public class ServerImpl implements Service {
    private final SharedQueue queue; 


    public ServerImpl(SharedQueue queue){
        this.queue = queue; 
    }


    @Override
    public void print(String path, String tipo){
        String msg = path +"-"+tipo; 
        try{
            queue.put(msg);
            System.out.println("[Server] Aggiunto in coda: " + msg); 

        }catch(InterruptedException e){
            Thread.currentThread().interrupt(); 
            
        }
    }
    
}
