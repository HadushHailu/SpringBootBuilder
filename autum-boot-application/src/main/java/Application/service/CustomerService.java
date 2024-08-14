package Application.service;

import Application.dao.ICustomerDAO;
import Application.domain.Customer;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Scheduled;
import org.autumframework.annotation.Service;
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
