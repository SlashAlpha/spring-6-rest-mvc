package slash.code.spring6restmvc.bootstrap_data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import slash.code.spring6restmvc.bootstrap.BootStrapData;
import slash.code.spring6restmvc.repositories.BeerRepository;
import slash.code.spring6restmvc.repositories.CustomerRepository;
import slash.code.spring6restmvc.services.BeerCSVService;
import slash.code.spring6restmvc.services.BeerCSVServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;


    @DataJpaTest
    @Import(BeerCSVServiceImpl.class)
    class BootstrapDataTest {

        @Autowired
        BeerRepository beerRepository;

        @Autowired
        CustomerRepository customerRepository;

        @Autowired
        BeerCSVService csvService;


        BootStrapData bootstrapData;

        @BeforeEach
        void setUp() {
            bootstrapData = new BootStrapData(beerRepository, customerRepository, csvService);
        }

        @Test
        void Testrun() throws Exception {
            bootstrapData.run(null);

            assertThat(beerRepository.count()).isEqualTo(2413);
            assertThat(customerRepository.count()).isEqualTo(3);
        }
    }

