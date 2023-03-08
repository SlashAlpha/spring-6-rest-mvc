package slash.code.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
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
import slash.code.spring6restmvc.config.SpringSecConfig;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
    public static final String USERNAME="user1";
    public static final String PASSWORD="password";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

   // @Disabled // just for demo purposes
    @Test
    void testUpdateBeerBadVersion() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);

        beerDTO.setBeerName("Updated Name");

        MvcResult result = mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME,PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        beerDTO.setBeerName("Updated Name 2");

        MvcResult result2 = mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .with(httpBasic(USERNAME,PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result2.getResponse().getStatus());
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
                     .with(httpBasic(USERNAME,PASSWORD))
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
        Page<BeerDTO> dtos = beerController.listBeers(null, null, false, 1, 2413);

        assertThat(dtos.getContent().size()).isEqualTo(1000);
    }
    @Test
    void findAllByBeerNameIsIgnoreCaseTest() {
        Page<Beer> list=beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);
        assertThat(list.getContent().size()).isEqualTo(336);

    }


    @Test
    void tesListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .with(httpBasic(USERNAME,PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryTrue() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .with(httpBasic(USERNAME,PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .with(httpBasic(USERNAME,PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void tesListBeersByStyleAndName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .with(httpBasic(USERNAME,PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)));
    }

    @Test
    void tesListBeersByStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .with(httpBasic(USERNAME,PASSWORD))
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(548)));
    }
    @Test
    void tesFailedAuth() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)

                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isUnauthorized());
              //  .andExpect(jsonPath("$.content.size()", is(548)));
    }

    @Test
    void tesListBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_URI)
                        .with(httpBasic(USERNAME,PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)));}

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
        Page<BeerDTO>dtos=beerController.listBeers(null,null, false, 1, 25);
        assertThat(dtos.getContent().size()).isEqualTo(0);

    }



}