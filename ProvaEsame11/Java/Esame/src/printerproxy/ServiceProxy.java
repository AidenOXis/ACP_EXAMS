package printerproxy;

import java.io.DataOutputStream;
import java.net.Socket;

import Iprinter.Service;

public class ServiceProxy implements Service {
    private final String ip;
    private final int port;

    public ServiceProxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void print(String path, String tipo) {
        String msg = path + "-" + tipo;
        try (Socket socket = new Socket(ip, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            out.writeUTF(msg);   // Scrive lunghezza + contenuto della stringa
            out.flush();         // Forza invio sul socket

            System.out.println("[Proxy] Inviato via DataOutputStream: " + msg);

        } catch (Exception e) {
            System.err.println("[Proxy] Errore di connessione TCP:");
            e.printStackTrace();
        }
    }
}
