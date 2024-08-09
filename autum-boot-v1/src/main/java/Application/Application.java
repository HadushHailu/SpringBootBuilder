package Application;

import Application.service.ICustomerService;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Service;
import org.autumframework.context.AutumApplication;

import java.util.List;

@Service
public class Application {
    @Autowired
    ICustomerService customerService;

    public void addCustomer(){
        customerService.addCustomer("Hadush", "had@gmail.com", "123-456-789");
    }

    public static void main(String[] args) {
        AutumApplication autumApplication = new AutumApplication("Application");
        autumApplication.run();

        Application application = (Application) autumApplication.getContext(Application.class);
        application.addCustomer();

    }
}
