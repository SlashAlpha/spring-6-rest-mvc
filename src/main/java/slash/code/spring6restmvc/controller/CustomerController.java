package slash.code.spring6restmvc.controller;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import slash.code.spring6restmvc.model.Customer;
import slash.code.spring6restmvc.services.CustomerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {


    CustomerService customerService;
    public static final String CUSTOMER_URI="/api/v1/customer";
    public static final String CUSTOMER_PATH_PARAM="{customerId}";
    public static final String CUSTOMER_PATH_ID=CUSTOMER_URI+"/"+CUSTOMER_PATH_PARAM;



    @RequestMapping(method = RequestMethod.GET)
    List<Customer> getAllCustomer(){
       return customerService.listCustomers();
    }

    @RequestMapping(value = CUSTOMER_PATH_PARAM,method = RequestMethod.GET)
    Customer getCustomerById(@PathVariable("customerId")  UUID customerId){
        log.debug("GetCustomer By Id in Customer Controller");
       return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<Customer> handlePost(@RequestBody Customer customer){

        HttpHeaders headers= new HttpHeaders();

        Customer savedCustomer=  customerService.savedNewCustomer(customer);
        headers.add("Location",CUSTOMER_URI+"/"+savedCustomer.getId().toString());
        return new ResponseEntity<Customer>(headers, HttpStatus.CREATED);

    }
    @PutMapping(CUSTOMER_PATH_PARAM)
    public ResponseEntity<Customer> updateById(@PathVariable("customerId") UUID customerId,@RequestBody Customer customer) {

        return new ResponseEntity<Customer>(customerService.updateCustomerById(customerId, customer), HttpStatus.OK);
    }

    @DeleteMapping(CUSTOMER_PATH_PARAM)
    public  ResponseEntity<Customer> deleteById(@PathVariable("customerId") UUID customerId){
        customerService.deleteCustomerById(customerId);

        return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);

    }

    @PatchMapping(CUSTOMER_PATH_PARAM)
    public ResponseEntity<Customer> patchById(@PathVariable("customerId") UUID customerId,@RequestBody Customer customer) {
                customerService.patchCustomerById(customerId,customer);
        return new ResponseEntity<Customer>( HttpStatus.OK);
    }



}
