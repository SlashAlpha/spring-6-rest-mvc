package slash.code.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import slash.code.spring6restmvc.entities.Beer;
import slash.code.spring6restmvc.mappers.BeerMapper;
import slash.code.spring6restmvc.model.BeerDTO;
import slash.code.spring6restmvc.model.BeerStyle;
import slash.code.spring6restmvc.repositories.BeerRepository;

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
    @Autowired
    BeerMapper beerMapper;


    @Transactional
    @Rollback
    @Test
    void patchIdFound() {
        Beer beer=beerRepository.findAll().get(0);
        BeerDTO beerDTO=beerMapper.beerToBeerDTO(beer);
        final String beerName= "UPDATED";
        beerDTO.setBeerName(beerName);
        ResponseEntity responseEntity=beerController.patchById(beer.getId(),beerDTO);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
        Beer patchedBeer= beerRepository.findById(beer.getId()).get();
        assertThat(patchedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void patchIdNotFound() {

        assertThrows(NotFoundException.class,()->{
            beerController.patchById(UUID.randomUUID(),BeerDTO.builder().build());
        });

    }

    @Transactional
    @Rollback
    @Test
    void deleteByIdFound() {
        Beer beer=beerRepository.findAll().get(0);
        ResponseEntity responseEntity=beerController.deleteById(beer.getId());

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(204);

        assertThat(beerRepository.findById(beer.getId()).isEmpty());


    }

    @Test
    void deleteByIdNotFound() {
        assertThrows(NotFoundException.class,()->{
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingBeerTest() {
        Beer beer=beerRepository.findAll().get(0);
        BeerDTO beerDTO=beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName= "UPDATED";
        beerDTO.setBeerName(beerName);
        ResponseEntity responseEntity=beerController.updateById(beer.getId(),beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));

        Beer updatedBeer= beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class,()->{
            beerController.updateById(UUID.randomUUID(),BeerDTO.builder().build());
        });
                    }

    @Rollback
    @Transactional
    @Test
    void saveBeerTest() {

        BeerDTO beerDTO=BeerDTO.builder()
                .beerName("crik")
                .beerStyle(BeerStyle.SAISON)
                .build();

        ResponseEntity responseEntity=beerController.handlePost(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locationUUID= responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID saveUUID =UUID.fromString(locationUUID[4]);
        Beer beer=beerRepository.findById(saveUUID).orElse(null);
        assertThat(beer).isNotNull();

    }

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