package slash.code.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slash.code.spring6restmvc.model.Beer;
import slash.code.spring6restmvc.services.BeerService;

import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@AllArgsConstructor
public class BeerController {

    private final BeerService beerService;

    @RequestMapping("/api/v1/beers")
    public List<Beer> listBeers(){
        return beerService.listBeers();
    }


    public Beer getBeerById(UUID id){
        log.debug("GetBeer By Id in Beer Controller");
       return beerService.getBeerById(id);
    }


}
