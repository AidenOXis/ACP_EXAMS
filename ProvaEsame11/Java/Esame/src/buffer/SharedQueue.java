package buffer;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class SharedQueue {
    private final LinkedList<String> buffer = new LinkedList<>(); 
    private final int size = 5; 



    private final Lock lock = new ReentrantLock(); 
    private final Condition notEmpty = lock.newCondition(); 
    private final Condition notFull = lock.newCondition(); 
    
    public void put(String msg) throws InterruptedException {
        lock.lock(); 
        try{
            while(buffer.size() == size){
                notFull.await(); 
            }
            buffer.add(msg); 
            System.out.println("[Queue] added: "+ msg); 
            notEmpty.signalAll();

        }finally{
            lock.unlock();
        }
    }
    public String take() throws InterruptedException {
        lock.lock(); 
        try{
            while(buffer.isEmpty()){
                notEmpty.await();
            }
            String msg = buffer.removeFirst(); 
            System.out.println("[QUeue] removed: " + msg); 
            notFull.signalAll(); 
            return msg; 
        }finally{
            lock.unlock();
        }
    }
}