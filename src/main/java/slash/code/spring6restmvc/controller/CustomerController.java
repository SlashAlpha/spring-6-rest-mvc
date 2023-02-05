package slash.code.spring6restmvc.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import slash.code.spring6restmvc.model.Beer;
import slash.code.spring6restmvc.model.Customer;
import slash.code.spring6restmvc.services.CustomerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Customer> getAllCustomer(){
       return customerService.listCustomers();
    }

    @RequestMapping(value = "{customerId}",method = RequestMethod.GET)
    Customer getCustomerById(@PathVariable("customerId")  UUID customerId){
        log.debug("GetCustomer By Id in Customer Controller");
       return customerService.getCustomerById(customerId);
    }

    @PostMapping
    public ResponseEntity<Customer> handlePost(@RequestBody Customer customer){

        HttpHeaders headers= new HttpHeaders();

        Customer savedCustomer=  customerService.savedNewCustomer(customer);
        headers.add("Location","/api/v1/customer/"+savedCustomer.getId().toString());
        return new ResponseEntity<Customer>(headers, HttpStatus.CREATED);

    }

}
