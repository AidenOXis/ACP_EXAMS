package user;

import java.util.Random;

import Iprinter.Service;
import printerproxy.ServiceProxy;

public class User {
    public static void main(String[] args) {
        String ip = "localhost";
        int port = 12345;

        Service proxy = new ServiceProxy(ip, port);

        String[] tipi = {"bw", "gs", "color"};
        String[] estensioni = {"doc", "txt"};
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int num = rand.nextInt(100);
            String tipo = tipi[rand.nextInt(tipi.length)];
            String est = estensioni[rand.nextInt(estensioni.length)];
            String path = "/user/file_" + num + "." + est;

            proxy.print(path, tipo);

            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }
}
