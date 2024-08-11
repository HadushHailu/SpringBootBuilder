package Application.integration;

import Application.AppConfig;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Service;
import org.autumframework.annotation.Value;

@Service
public class EmailSender implements IEmailSender{
    @Value("app.outgoingmail")
    private String outgoingmail;

    public void sendEmail(){
        System.out.println("Email sent to="+outgoingmail);
    }
}
