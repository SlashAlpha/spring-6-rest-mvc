package slash.code.spring6restmvc.services;

import slash.code.spring6restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();
    Customer getCustomerById(UUID id);
    Customer savedNewCustomer(Customer customer);

}
