package server;

public class LoggingServer {
    public static void main(String[] args){
        System.out.println("[LoggingServer]Server started...");
        if(args.length != 1){
            System.out.println("Uso: java LoggingServer <porta>");
            return; 
        }
        int port = Integer.parseInt(args[0]); 
        LoggingImpl impl = new LoggingImpl(port); 
        impl.runSkeleton();
    }
    
}

