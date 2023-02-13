package slash.code.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import slash.code.spring6restmvc.bootstrap.BootStrapData;
import slash.code.spring6restmvc.entities.Beer;
import slash.code.spring6restmvc.mappers.BeerMapper;
import slash.code.spring6restmvc.model.BeerDTO;
import slash.code.spring6restmvc.model.BeerStyle;
import slash.code.spring6restmvc.repositories.BeerRepository;
import slash.code.spring6restmvc.services.BeerCSVServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@Import({BootStrapData.class, BeerCSVServiceImpl.class})
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

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
    void testPatchBeerBadName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

     MvcResult result= mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest()) .andExpect(jsonPath("$.length()",is(1)))
             .andReturn();
        System.out.println(result.getResponse().getContentAsString());


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
        List<BeerDTO>dtos=beerController.listBeers(null,null, false);
        assertThat(dtos.size()).isEqualTo(2413);
    }
    @Test
    void findAllByBeerNameIsIgnoreCaseTest() {
        List<Beer> list=beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
        assertThat(list.size()).isEqualTo(336);

    }
    @Test
    void tesListBeersByStyleAndName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310)));
    }
    @Test
    void tesListBeersByStyleAndNameShowInventoryTrue() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310)))
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310)))
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.nullValue()));
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
        List<BeerDTO>dtos=beerController.listBeers(null,null, false);
        assertThat(dtos.size()).isEqualTo(0);

    }

    @Test
    void listBeerByNameTest() throws Exception {
        mockMvc.perform(get(beerController.BEER_URI).queryParam("beerName","IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(336)));

    }
    @Test
    void listBeerByStyleTest() throws Exception {
        mockMvc.perform(get(beerController.BEER_URI).queryParam("beerStyle",BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(548)));

    }
}