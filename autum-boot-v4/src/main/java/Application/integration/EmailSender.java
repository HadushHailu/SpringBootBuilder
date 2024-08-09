package Application.integration;

import org.autumframework.annotation.Service;

@Service
public class EmailSender implements IEmailSender{
    public void sendEmail(){
        System.out.println("Email sent");
    }
}
