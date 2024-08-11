package Application.integration;

import org.autumframework.annotation.Service;

@Service
public class Logger implements ILogger{
    public void log(){
        System.out.println("logging");
    }
}
