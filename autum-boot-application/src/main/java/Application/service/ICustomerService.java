package Application.service;

import Application.domain.Customer;

public interface ICustomerService {
    public void addCustomer(String name, String email, String phone);
    public Customer findCustomer(String name);
}
