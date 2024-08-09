package Application;

import Application.service.ICustomerService;
import Application.service.IProductService;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Service;
import org.autumframework.context.AutumApplication;

import java.util.List;

@Service
public class Application {
    @Autowired
    ICustomerService customerService;

    @Autowired
    IProductService productService;

    public void addCustomer(){
        customerService.addCustomer("Hadush", "had@gmail.com", "123-456-789");
    }

    public void addProduct(){
        productService.addProduct("XGL-120", 345.9, 13);
    }

    public static void main(String[] args) {
        AutumApplication autumApplication = new AutumApplication("Application");
        autumApplication.run();

        Application application = (Application) autumApplication.getContext(Application.class);
        application.addCustomer();
        application.addProduct();

    }
}
