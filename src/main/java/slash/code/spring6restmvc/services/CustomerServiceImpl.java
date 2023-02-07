package slash.code.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import slash.code.spring6restmvc.model.Beer;
import slash.code.spring6restmvc.model.Customer;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID,Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap =new HashMap<>();
        Customer customer1=
                Customer.builder()
                        .id(UUID.randomUUID())
                        .version(1)
                        .customerName("John")
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build();
        Customer customer2=
                Customer.builder()
                        .id(UUID.randomUUID())
                        .version(2)
                        .customerName("Peter")
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build();
        Customer customer3=
                Customer.builder()
                        .id(UUID.randomUUID())
                        .version(3)
                        .customerName("Margaret")
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build();

        customerMap.put(customer1.getId(),customer1);
        customerMap.put(customer2.getId(),customer2);
        customerMap.put(customer3.getId(),customer3);

    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<Customer> getCustomerById(UUID uuid) {
        return Optional.of(customerMap.get(uuid));
    }

    @Override
    public Customer savedNewCustomer(Customer customer) {

      Customer savedCustomer=  Customer.builder()
                .id(UUID.randomUUID())
                .version(customer.getVersion())
                .customerName(customer.getCustomerName())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
      customerMap.put(savedCustomer.getId(),savedCustomer);
        return savedCustomer;
    }

    @Override
    public Customer updateCustomerById(UUID customerId, Customer customer) {
        Customer existingCustomer= customerMap.get(customerId);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(customer.getVersion());
        existingCustomer.setUpdateDate(LocalDateTime.now());
       return customerMap.put(customerId,existingCustomer);

    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existing = customerMap.get(customerId);

        if (StringUtils.hasText(customer.getCustomerName())){
            existing.setCustomerName(customer.getCustomerName());
        }
        if (StringUtils.hasText(customer.getVersion().toString())){
            existing.setVersion(customer.getVersion());
        }
    }
}
