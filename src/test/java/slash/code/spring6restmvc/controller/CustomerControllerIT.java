package slash.code.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import slash.code.spring6restmvc.entities.Beer;
import slash.code.spring6restmvc.entities.Customer;
import slash.code.spring6restmvc.mappers.CustomerMapper;
import slash.code.spring6restmvc.model.CustomerDTO;
import slash.code.spring6restmvc.repositories.CustomerRepository;
import slash.code.spring6restmvc.services.CustomerService;

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

    @Autowired
    CustomerMapper customerMapper;


    @Transactional
    @Rollback
    @Test
    void patchIdFound() {
        Customer customer=customerRepository.findAll().get(0);
        CustomerDTO customerDTO=customerMapper.customerToCustomerDTO(customer);
        final String customerName= "UPDATED";
        customerDTO.setCustomerName(customerName);
        ResponseEntity responseEntity=customerController.patchById(customerDTO.getId(),customerDTO);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);

        Customer foundCustomer= customerRepository.findAll().get(0);
        assertThat(foundCustomer.getCustomerName()).isEqualTo(customerName);

    }

    @Test
    void patchIdNotFound() {

        assertThrows(NotFoundException.class,()->{

            customerController.patchById(UUID.randomUUID(),CustomerDTO.builder().build());

        });
    }

    @Transactional
    @Rollback
    @Test
    void deleteIdFound() {
        Customer customer=customerRepository.findAll().get(0);
        ResponseEntity responseEntity=customerController.deleteById(customer.getId());

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);

        assertThat(customerRepository.findById(customer.getId()).isEmpty());

    }

    @Test
    void deleteIdNotFound() {
        assertThrows(NotFoundException.class,()->{

                customerController.deleteById(UUID.randomUUID());});
    }

    @Test
    void updateByIdNotFound() {

        assertThrows(NotFoundException.class,()->{
            customerController.updateById(UUID.randomUUID(),CustomerDTO.builder().build());
        });

    }

    @Transactional
    @Rollback
    @Test
    void updateByIdFound() {
        Customer customer=customerRepository.findAll().get(0);
        CustomerDTO customerDTO=customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName= "UPDATED";
        customerDTO.setCustomerName(customerName);
        ResponseEntity responseEntity=customerController.updateById(customer.getId(),customerDTO);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        Customer customer1=customerRepository.findById(customer.getId()).get();
        assertThat(customer1.getCustomerName()).isEqualTo(customerName);
    }

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