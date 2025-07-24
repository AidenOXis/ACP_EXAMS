package PrinterProxy;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import IPrinter.*;


public class ServiceProxy implements Service {
    private final String ip; 
    private final int port; 



    public ServiceProxy(String ip, int port){
        this.ip = ip; 
        this.port = port; 
    }



    @Override
    public void print(String path, String tipo){
        String msg = path + "-" + tipo; 
        try (DatagramSocket socket = new DatagramSocket()){
            byte[] buf = msg.getBytes(); 
            DatagramPacket packet = new DatagramPacket(buf, buf.length,InetAddress.getByName(ip), port); 
            socket.send(packet); 
            System.out.println("[Proxy] Inviato: "+ msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
