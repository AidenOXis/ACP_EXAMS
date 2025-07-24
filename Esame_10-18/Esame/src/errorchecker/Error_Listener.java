package errorchecker;

import java.io.FileWriter;
import java.io.IOException;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;


public class Error_Listener implements MessageListener {


    private String keyword; 

    public Error_Listener(String keyword){
        this.keyword = keyword;
    }


    @Override
    public void onMessage(Message message){
        try{
            if(message instanceof MapMessage){
                MapMessage map = (MapMessage) message; 
                String msg = null;
                try {
                    msg = map.getString("msg");
                } catch (javax.jms.JMSException e) {
                    e.printStackTrace();
                    return;
                }

                if(msg.contains(keyword)){
                    System.out.println(("MATCH: " +msg));
                    try(FileWriter fw = new FileWriter("error.txt",true)){
                        fw.write(msg +"\n");
                    }
                }else{
                    System.out.println("Ignorato. " + msg); 
                }
            }
        }catch (IOException e ){
            e.printStackTrace();
        }
    }
    
}
