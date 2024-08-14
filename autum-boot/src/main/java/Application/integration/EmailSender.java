package Application.integration;

import Application.service.CustomerEvent;
import org.autumframework.annotation.*;

@Service
public class EmailSender implements IEmailSender{
    @Value("app.outgoingmail")
    private String outgoingmail;

    @Async
    public void sendEmail() {
        try {

            Thread thread = new Thread();
            thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Email sent to="+outgoingmail);
    }

    @EventListener
    public void log(CustomerEvent customerEvent) {
        System.out.println("EmailSender: eventListener Called");
        try{
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Email event:" + customerEvent);;
    }
}
