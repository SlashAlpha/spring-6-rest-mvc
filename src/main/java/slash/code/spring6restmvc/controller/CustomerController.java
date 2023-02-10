package slash.code.spring6restmvc.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import slash.code.spring6restmvc.model.CustomerDTO;
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
    List<CustomerDTO> getAllCustomer(){
       return customerService.listCustomers();
    }

    @RequestMapping(value = CUSTOMER_PATH_PARAM,method = RequestMethod.GET)
    CustomerDTO getCustomerById(@PathVariable("customerId")  UUID customerId){
        log.debug("GetCustomer By Id in Customer Controller");
       return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> handlePost(@RequestBody CustomerDTO customer){

        HttpHeaders headers= new HttpHeaders();

        CustomerDTO savedCustomer=  customerService.savedNewCustomer(customer);
        headers.add("Location",CUSTOMER_URI+"/"+savedCustomer.getId().toString());
        return new ResponseEntity<CustomerDTO>(headers, HttpStatus.CREATED);

    }
    @PutMapping(CUSTOMER_PATH_PARAM)
    public ResponseEntity updateById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {

        if(customerService.updateCustomerById(customerId,customer).isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity<CustomerDTO>(HttpStatus.OK);
    }

    @DeleteMapping(CUSTOMER_PATH_PARAM)
    public  ResponseEntity<CustomerDTO> deleteById(@PathVariable("customerId") UUID customerId){
        if (!customerService.deleteCustomerById(customerId)){
            throw new NotFoundException();
        }


        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PatchMapping(CUSTOMER_PATH_PARAM)
    public ResponseEntity<CustomerDTO> patchById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {

        if(customerService.patchCustomerById(customerId,customer).isEmpty()){
            throw new NotFoundException();
        }

                customerService.patchCustomerById(customerId,customer);
        return new ResponseEntity<CustomerDTO>( HttpStatus.OK);
    }



}
