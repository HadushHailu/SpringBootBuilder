package Application.service;

import Application.domain.Customer;

public class CustomerEvent {

    private Customer customer;

    public CustomerEvent(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "CustomerEvent{" +
               "customer=" + customer +
               '}';
    }
}
