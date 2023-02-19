package slash.code.spring6restmvc.repositories;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import slash.code.spring6restmvc.entities.Beer;
import slash.code.spring6restmvc.entities.BeerOrder;
import slash.code.spring6restmvc.entities.BeerOrderShipment;
import slash.code.spring6restmvc.entities.Customer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository testCustomerRepository;

    @Autowired
    BeerRepository testBeerRepository;

    Beer testBeer;
    Customer testCustomer;

    @BeforeEach
    void setUp() {
        testBeer=testBeerRepository.findAll().get(0);
        testCustomer=testCustomerRepository.findAll().get(0);
    }



    @Transactional
    @Test
    void testBeerOrder() {
        BeerOrder beerOrder=BeerOrder.builder()
                .customerRef("test Order")
                .customer(testCustomer)
                .beerOrderShipment(BeerOrderShipment.builder().trackingNumber("12345R").build())
                .build();
        BeerOrder savedBeerOrder=beerOrderRepository.save(beerOrder);

        System.out.println(savedBeerOrder.getCustomerRef());
    }
}