package infofilter;


import java.io.FileWriter;
import java.io.IOException;
import javax.jms.*;
import javax.jms.Message;
import javax.jms.MessageListener;


public class Info_Listener implements MessageListener {


    private String keyword; 

    public Info_Listener(String keyword){
        this.keyword = keyword;
    }


    @Override
    public void onMessage(Message message){
        try{
            if(message instanceof MapMessage){
                MapMessage map = (MapMessage) message; 
                String msg = map.getString("msg");

                if(msg.contains(keyword)){
                    System.out.println(("MATCH: " +msg));
                    try(FileWriter fw = new FileWriter("error.txt",true)){
                        fw.write(msg +"\n");
                    }
                }else{
                    System.out.println("Ignorato. " + msg); 
                }
            }
        }catch (JMSException | IOException e){
            e.printStackTrace();
        }
    }
    
}
