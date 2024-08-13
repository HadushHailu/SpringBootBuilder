package Application.integration;

import Application.service.CustomerEvent;
import org.autumframework.annotation.EventListener;
import org.autumframework.annotation.Service;

@Service
public class Logger implements ILogger{
    public void log(){
        System.out.println("logging");
    }

    @EventListener
    public void eventLog(CustomerEvent customerEvent) {
        System.out.println("Logger with event :" + customerEvent);;
    }
}
