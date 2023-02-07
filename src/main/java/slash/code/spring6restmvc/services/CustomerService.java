package slash.code.spring6restmvc.services;

import slash.code.spring6restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();
    Optional<Customer> getCustomerById(UUID id);
    Customer savedNewCustomer(Customer customer);
    Customer updateCustomerById(UUID customerId, Customer customer);
    void deleteCustomerById(UUID customerId);
    void patchCustomerById(UUID customerId,Customer customer);

}
