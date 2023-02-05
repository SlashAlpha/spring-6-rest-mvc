package slash.code.spring6restmvc.services;

import org.springframework.stereotype.Service;
import slash.code.spring6restmvc.model.Beer;
import slash.code.spring6restmvc.model.BeerStyle;
import slash.code.spring6restmvc.model.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID,Customer> customerList;

    public CustomerServiceImpl() {
        this.customerList=new HashMap<>();
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

        customerList.put(customer1.getId(),customer1);
        customerList.put(customer2.getId(),customer2);
        customerList.put(customer3.getId(),customer3);

    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customerList.values());
    }

    @Override
    public Customer getCustomerById(UUID uuid) {
        return customerList.get(uuid);
    }
}
