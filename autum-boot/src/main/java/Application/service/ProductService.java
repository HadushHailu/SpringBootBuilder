package Application.service;

import Application.dao.IProductDAO;
import Application.dao.ProductDAO;
import Application.domain.Product;
import Application.integration.IEmailSender;
import Application.integration.ILogger;
import Application.integration.Logger;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Qualifier;
import org.autumframework.annotation.Scheduled;
import org.autumframework.annotation.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service(name = "productService")
public class ProductService implements IProductService{
    @Autowired
    private IProductDAO iProductDAO;
    private ILogger logger;
    private IEmailSender emailSender;

    @Autowired
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    public ProductService() {
    }

    @Autowired
    ProductService(IEmailSender emailSender){
        this.emailSender = emailSender;
    }

    @Scheduled(cron = "5 0")
    public void scheduledLogAllProduct(){
        System.out.println("====== Scheduled logging: "+ LocalTime.now() +" =====");
        logger.log("All products");
    }

    public void addProduct(String id, double price, int quantity){
        Product product = new Product(id, price, quantity);
        iProductDAO.save(product);
    }
}
