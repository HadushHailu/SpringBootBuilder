package Application.dao;

import Application.domain.Customer;

public interface ICustomerDAO {
    public void save(Customer customer);
    public Customer find(String name);
}
