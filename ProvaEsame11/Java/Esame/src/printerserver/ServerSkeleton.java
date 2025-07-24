package printerserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import Iprinter.Service;

public abstract class ServerSkeleton {
    private final int port;
    protected final Service service;

    public ServerSkeleton(int port, Service service) {
        this.port = port;
        this.service = service;
    }

    public void runSkeleton() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[Skeleton] In ascolto su porta TCP " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    try (
                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()))
                    ) {
                        String msg = reader.readLine();
                        if (msg != null && msg.contains("-")) {
                            String[] parts = msg.split("-");
                            String path = parts[0];
                            String tipo = parts[1];
                            service.print(path, tipo);
                        } else {
                            System.out.println("[Skeleton] Messaggio malformato");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
