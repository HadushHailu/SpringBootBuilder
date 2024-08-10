package Application.integration;

import Application.service.AppConfig;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Service;
import org.autumframework.annotation.Value;

@Service
public class EmailSender implements IEmailSender{

    @Value("app.name")
    private String appName;

    @Value("app.timeout")
    private int timeout;

    @Value("app.outgoingmail")
    private String outgoingmail;

    @Autowired
    private AppConfig config;

    public void sendEmail(){
        System.out.println("Email sent");
        System.out.println("App Name:"+appName+", Email:"+outgoingmail );
        System.out.println("App Name:"+config.getName()+", Timeout:"+config.getTimeout());
    }
}
