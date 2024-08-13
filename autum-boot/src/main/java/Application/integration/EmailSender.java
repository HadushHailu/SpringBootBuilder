package Application.integration;

import Application.AppConfig;
import Application.service.CustomerEvent;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.EventListener;
import org.autumframework.annotation.Service;
import org.autumframework.annotation.Value;

@Service
public class EmailSender implements IEmailSender{
    @Value("app.outgoingmail")
    private String outgoingmail;

    public void sendEmail(){
        System.out.println("Email sent to="+outgoingmail);
    }

    @EventListener
    public void log(CustomerEvent customerEvent) {
        System.out.println("Email event:" + customerEvent);;
    }
}
