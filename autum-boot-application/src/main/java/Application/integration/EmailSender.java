package Application.integration;

import Application.service.CustomerEvent;
import org.autumframework.annotation.*;

@Service
public class EmailSender implements IEmailSender{
    @Value("app.outgoingmail")
    private String outgoingmail;

    public void sendEmail() {
        System.out.println("EmailSender: Email sent to="+outgoingmail);
    }

    @Async
    @EventListener
    public void eventLog(CustomerEvent customerEvent) {
        System.out.println("EmailSender: eventListener Called:");
        try{
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Email event:" + customerEvent);;
    }
}
