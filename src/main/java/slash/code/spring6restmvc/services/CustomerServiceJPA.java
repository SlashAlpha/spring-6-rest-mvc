package slash.code.spring6restmvc.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import slash.code.spring6restmvc.mappers.CustomerMapper;
import slash.code.spring6restmvc.model.CustomerDTO;
import slash.code.spring6restmvc.repositories.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService{

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDTO(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDTO savedNewCustomer(CustomerDTO customer) {
        return null;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        AtomicReference<Optional<CustomerDTO>> atomicReference=new AtomicReference<>();
        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setCustomerName(customerDTO.getCustomerName());
            foundCustomer.setVersion(customerDTO.getVersion());
            foundCustomer.setUpdateDate(LocalDateTime.now());
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDTO(customerRepository.save(foundCustomer))));
        },()->atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
            if(customerRepository.existsById(customerId)){
                customerRepository.deleteById(customerId);
                return true;
            }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {

            AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

            customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
                if (StringUtils.hasText(customer.getCustomerName())){
                    foundCustomer.setCustomerName(customer.getCustomerName());
                }
                atomicReference.set(Optional.of(customerMapper
                        .customerToCustomerDTO(customerRepository.save(foundCustomer))));
            }, () -> {
                atomicReference.set(Optional.empty());
            });

            return atomicReference.get();
        }



}
