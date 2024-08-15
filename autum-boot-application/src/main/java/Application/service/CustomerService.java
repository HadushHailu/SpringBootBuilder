package Application.service;

import Application.dao.ICustomerDAO;
import Application.domain.Customer;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Scheduled;
import org.autumframework.annotation.Service;
import org.autumframework.event.ApplicationEvent;

@Service
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

    public Customer findCustomer(String name){
        Customer customer = customerDAO.find(name);
        System.out.println(customer);
        return customer;
    }

}
