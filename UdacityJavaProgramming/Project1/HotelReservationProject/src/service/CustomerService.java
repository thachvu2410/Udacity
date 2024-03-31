package service;

import model.Customer;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerService {
    private static final CustomerService CUSTOMER_SERVICE = new CustomerService();
    private static HashMap<String, Customer> customers = new HashMap<>();

    private CustomerService(){

    }

    public static CustomerService getCustomerService(){
        return CUSTOMER_SERVICE;
    }

    public void addCustomer(String email, String firstName, String lastName){
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(customer.getEmail(), customer);
        System.out.println("Add customer successfully!!!");
    }

    public Customer getCustomer(String customerEmail){
        for (String email: customers.keySet()
             ) {
            if (email.equals(customerEmail)){
                return customers.get(customerEmail);
            }
        }
        return null;
    }

    public ArrayList<Customer> getAllCustomers(){
        return new ArrayList<>(customers.values());
    }
}
