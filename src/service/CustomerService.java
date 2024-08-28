package service;

import collection.HotelCollection;
import model.Customer;

import java.util.Collection;

public class CustomerService {
    private static final CustomerService instance = new CustomerService();
    public static CustomerService getSingleton() {
        return instance;
    }

    public static void addCustomer(String firstName, String lastName, String email) {
        Customer customer = new Customer(firstName, lastName, email);
        HotelCollection.customers.put(email, customer);
    }

    public static Customer getCustomer(String customerEmail) {
        return HotelCollection.customers.get(customerEmail);
    }

    public static Collection<Customer> getAllCustomers() {
        return HotelCollection.customers.values();
    }
}
