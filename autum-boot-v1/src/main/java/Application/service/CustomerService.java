package Application.service;

import Application.dao.CustomerDAO;
import Application.dao.ICustomerDAO;
import Application.domain.Customer;
import org.autumframework.annotation.Autowired;
import org.autumframework.annotation.Service;

@Service(name = "customerService")
public class CustomerService implements ICustomerService{
    @Autowired
    private ICustomerDAO customerDAO;

    public void addCustomer(String name, String email, String phone){
        Customer customer = new Customer(name, email, phone);
        customerDAO.save(customer);
    }

}
