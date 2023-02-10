package slash.code.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import slash.code.spring6restmvc.entities.Beer;
import slash.code.spring6restmvc.model.BeerDTO;
import slash.code.spring6restmvc.repositories.BeerRepository;

import java.rmi.NotBoundException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerIdNotFound() {
        assertThrows(NotFoundException.class,()->{
            beerController.getBeerById(UUID.randomUUID());
        });


    }

    @Test
    void testListBeers() {
        List<BeerDTO>dtos=beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Test
    void getBeerByIdTest() {
        Beer beer=beerRepository.findAll().get(0);
        BeerDTO dto=beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO>dtos=beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);

    }
}