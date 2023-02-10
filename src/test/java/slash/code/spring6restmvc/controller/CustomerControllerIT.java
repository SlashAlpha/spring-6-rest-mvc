package slash.code.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import slash.code.spring6restmvc.entities.Customer;
import slash.code.spring6restmvc.model.CustomerDTO;
import slash.code.spring6restmvc.repositories.CustomerRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testFindAll() {
       List<CustomerDTO> dtoList=customerController.getAllCustomer();
        assertThat(dtoList.size()).isEqualTo(3);
    }

    @Test
    void testGetCustomerById() {
        Customer customer=customerRepository.findAll().get(0);
        CustomerDTO customerDTO=customerController.getCustomerById(customer.getId());
        assertThat(customerDTO).isNotNull();

    }

    @Test
    void testCustomerIdNotFound() {
        assertThrows(NotFoundException.class,()->{
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtoList=customerController.getAllCustomer();
        assertThat(dtoList.size()).isEqualTo(0);

    }
}