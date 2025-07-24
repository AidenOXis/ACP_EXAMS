package PrinterServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import IPrinter.Service;

public class ServerSkeleton {
    private final int port; 
    private final Service service; 

    
    public ServerSkeleton(int port, Service service){
        this.port = port;
        this.service = service;
    }
    public void runSkeleton() throws java.net.SocketException, java.io.IOException {
        try(DatagramSocket socket = new DatagramSocket(port)){
            System.out.println("[Skeleton] In ascolto su UDP porta " + port);
            byte[] buf = new byte[1024]; 


            while(true){
                DatagramPacket packet = new DatagramPacket(buf,buf.length); 
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if(msg.contains("-")){
                    String[] parts = msg.split("-"); 
                    if(parts.length == 2){
                        service.print(parts[0],parts[1]);
                    }
                }else {
                    System.out.println("[Skeleton] Messaggio malformato: "+ msg); 
                }
            }
        }
    }
    
}
