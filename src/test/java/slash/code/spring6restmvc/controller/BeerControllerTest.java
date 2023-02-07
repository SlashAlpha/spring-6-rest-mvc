package slash.code.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import slash.code.spring6restmvc.model.Beer;
import slash.code.spring6restmvc.services.BeerService;
import slash.code.spring6restmvc.services.BeerServiceImpl;


import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
@WebMvcTest(BeerController.class)
class BeerControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Captor
    ArgumentCaptor<UUID> argumentCaptor;

    @Autowired
    ObjectMapper objectMapper;

    BeerServiceImpl beerServiceImpl= new BeerServiceImpl();


    @Test
    void getBeerById() throws Exception {

        Beer testBeer=beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);


        mockMvc.perform(get("/api/v1/beer/"+ testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName",is(testBeer.getBeerName())));
    }

    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));


    }

    @BeforeEach
    void setUp() {
        beerServiceImpl=new BeerServiceImpl();
    }

    @Test
    void createNewBeerTest() throws Exception {


        Beer beer=beerServiceImpl.listBeers().get(0);
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveNewBeer(any(Beer.class)))
                .willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        System.out.println(objectMapper.writeValueAsString(beer));

    }

    @Test
    void updateBeerTest() throws Exception {
        Beer beer=beerServiceImpl.listBeers().get(0);

//        given(beerService.saveNewBeer(any(Beer.class)))
//                .willReturn(beerServiceImpl.listBeers().get(0));

        mockMvc.perform(put("/api/v1/beer/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                        .andExpect(status().isOk());

        verify(beerService).updateBeerById(any(UUID.class),any(Beer.class));


    }

    @Test
    void deleteBeerTest() throws Exception {
        Beer beer=beerServiceImpl.listBeers().get(0);


        mockMvc.perform(delete("/api/v1/beer/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        //verfify the UUID argument(originally the uri path variable) passed in the delete method of the controller

        verify(beerService).deleteById(argumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(argumentCaptor.getValue());

    }
}