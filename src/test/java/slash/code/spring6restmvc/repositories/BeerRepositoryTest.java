package slash.code.spring6restmvc.repositories;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import slash.code.spring6restmvc.bootstrap.BootStrapData;
import slash.code.spring6restmvc.entities.Beer;
import slash.code.spring6restmvc.model.BeerStyle;
import slash.code.spring6restmvc.services.BeerCSVServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootStrapData.class, BeerCSVServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer=  beerRepository.save(Beer.builder()
                .beerName("La Crik")
                        .beerStyle(BeerStyle.ALE)
                        .upc("12345")
                        .price(new BigDecimal(12))
                .build());

        beerRepository.flush();
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class,()->{
            Beer savedBeer=  beerRepository.save(Beer.builder()
                    .beerName("La Crik 1293847219847921847912347109234709128347129034712904712094712093471290347913")
                    .beerStyle(BeerStyle.ALE)
                    .upc("12345")
                    .price(new BigDecimal(12))
                    .build());

            beerRepository.flush();

        });

    }

    @Test
    void findAllByBeerNameIsIgnoreCaseTest() {
        Page<Beer> list=beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);
        assertThat(list.getContent().size()).isEqualTo(336);

    }
}