package slash.code.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import slash.code.spring6restmvc.entities.Customer;
import slash.code.spring6restmvc.model.CustomerDTO;

@Mapper
public interface CustomerMapper {

    Customer customerDTOTOCustomer(CustomerDTO customerDTO);
    CustomerDTO customerToCustomerDTO(Customer customer);

}
