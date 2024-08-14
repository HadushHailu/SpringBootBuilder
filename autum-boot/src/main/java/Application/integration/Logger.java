package Application.integration;

import Application.service.CustomerEvent;
import org.autumframework.annotation.Async;
import org.autumframework.annotation.EventListener;
import org.autumframework.annotation.Service;

@Service
public class Logger implements ILogger{
    public void log(String message){
        System.out.println("Logger: logging="+message);
    }

    @Async
    @EventListener
    public void eventLog(CustomerEvent customerEvent) {
        System.out.println("Logger: eventListener Called:");
        try{
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Logger with event :" + customerEvent);
    }
}
