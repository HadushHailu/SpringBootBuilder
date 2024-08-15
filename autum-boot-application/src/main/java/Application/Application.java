package Application;

import Application.config.ApplicationConfiguration;
import Application.service.ICustomerService;
import Application.service.IProductService;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Service;
import org.autumframework.context.AutumApplication;

import java.io.Serial;

@Service
public class Application implements Runnable{
    @Autowired
    ICustomerService customerService;

    @Autowired
    IProductService productService;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public static void main(String[] args) {
        AutumApplication.run(Application.class, args);
    }

    @Override
    public void run() {
        this.autumLogo();
        this.addCustomer();
        this.addProduct();
        this.findCustomer();

        System.out.println("Autum Application="+applicationConfiguration);
    }

    public void autumLogo(){
        System.out.println("    **        ******     ******     **   **    ******   ");
        System.out.println("  **  **      **  **     **  **     **   **    **  **   ");
        System.out.println(" ********     ******     ******     ******    ******   ");
        System.out.println(" **    **     **  **     **  **       **      **  **   ");
        System.out.println(" **    **     ******     **  **       **      ******   ");
    }

    public void addCustomer(){
        customerService.addCustomer("John", "johnDao@gmail.com", "123-456-789");
    }

    public void findCustomer(){
        customerService.findCustomer("John");
    }

    public void addProduct(){
        productService.addProduct("XGL-120", 345.9, 13);
    }
}
