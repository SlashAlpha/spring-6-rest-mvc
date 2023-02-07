package slash.code.spring6restmvc.services;

import slash.code.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);
    CustomerDTO savedNewCustomer(CustomerDTO customer);
    CustomerDTO updateCustomerById(UUID customerId, CustomerDTO customer);
    void deleteCustomerById(UUID customerId);
    void patchCustomerById(UUID customerId, CustomerDTO customer);

}
