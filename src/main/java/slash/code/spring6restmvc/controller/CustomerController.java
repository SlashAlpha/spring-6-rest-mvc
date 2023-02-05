package slash.code.spring6restmvc.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import slash.code.spring6restmvc.model.Customer;
import slash.code.spring6restmvc.services.CustomerService;

import java.util.List;
import java.util.UUID;

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
       return customerService.getCustomerById(customerId);
    }

}
