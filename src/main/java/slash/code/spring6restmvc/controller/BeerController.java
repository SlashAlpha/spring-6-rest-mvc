package slash.code.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import slash.code.spring6restmvc.model.Beer;
import slash.code.spring6restmvc.services.BeerService;

import java.util.UUID;
@Slf4j
@Controller
@AllArgsConstructor
public class BeerController {

    private final BeerService beerService;


    public Beer getBeerById(UUID id){
        log.debug("GetBeer By Id in Beer Controller");
       return beerService.getBeerById(id);
    }


}
