package Application.service;

import Application.AppConfig;
import Application.dao.CustomerDAO;
import Application.dao.ICustomerDAO;
import Application.domain.Customer;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Qualifier;
import org.autumframework.annotation.Service;
import org.autumframework.annotation.Value;
import org.autumframework.event.ApplicationEvent;

@Service(name = "customerService")
public class CustomerService implements ICustomerService{
    @Autowired
    private ICustomerDAO customerDAO;

    @Autowired
    private ApplicationEvent event;

    public void addCustomer(String name, String email, String phone){
        Customer customer = new Customer(name, email, phone);
        customerDAO.save(customer);

        event.publishEvent(new CustomerEvent(customer));
    }

}
