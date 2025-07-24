package service;

public class Service {


    private static final int NUM_ENTRY = 10; 

    public static void main(String[] args){


        String ip_address = args[0]; 
        int port = Integer.valueOf(args[1]); 

        Thread threads[] = new Thread[NUM_ENTRY];

        System.out.println("Avvio client"); 

        for (int i = 0; i<NUM_ENTRY; i++){
            threads[i] = new ServiceThread(ip_address,port); 
            threads[i].start();

        }
        System.out.println("Waiting for thread termination"); 

        for (int i = 0; i<NUM_ENTRY; i++){
            try{
                threads[i].join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
    }
    
}
}
