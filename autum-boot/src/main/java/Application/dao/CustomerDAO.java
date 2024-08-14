package Application.dao;

import Application.domain.Customer;
import org.autumframework.annotation.Profile;
import org.autumframework.annotation.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CustomerDAO implements ICustomerDAO {
    private HashMap<String, Customer> customerHashMap = new HashMap<>();

    public void save(Customer customer){
        customerHashMap.put(customer.getName(), customer);
        System.out.println("customer saved!");
    }
    public Customer find(String name){
        return customerHashMap.get(name);
    }
}
