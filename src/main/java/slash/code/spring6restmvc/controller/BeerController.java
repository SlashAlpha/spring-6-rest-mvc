package slash.code.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import slash.code.spring6restmvc.model.Beer;
import slash.code.spring6restmvc.services.BeerService;

import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/beer")
public class BeerController {

    public static final String BEER_URI="/api/v1/beer";
    public static final String BEER_PATH_PARAM="{beerId}";
    public static final String BEER_PATH_ID = BEER_URI + "/"+BEER_PATH_PARAM;

    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers(){
        return beerService.listBeers();
    }

    @RequestMapping(value = BEER_PATH_PARAM,method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId){
        log.debug("GetBeer By Id in Beer Controller");
       return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Beer> handlePost(@RequestBody Beer newBeer){

        HttpHeaders headers= new HttpHeaders();

      Beer savedBeer=  beerService.saveNewBeer(newBeer);
        headers.add("Location",BEER_URI+"/"+savedBeer.getId().toString());
       return new ResponseEntity<Beer>(headers,HttpStatus.CREATED);

    }

    @PutMapping(BEER_PATH_PARAM)
    public ResponseEntity<Beer> updateById(@PathVariable("beerId") UUID beerId,@RequestBody Beer beer){


        return new ResponseEntity<Beer>(beerService.updateBeerById(beerId,beer),HttpStatus.OK);

    }

    @DeleteMapping(BEER_PATH_PARAM)
    public  ResponseEntity<Beer> deleteById(@PathVariable("beerId")UUID beerId){
        beerService.deleteById(beerId);
        return new ResponseEntity<Beer>(HttpStatus.NO_CONTENT);

    }
    @PatchMapping(BEER_PATH_PARAM)
    public ResponseEntity<Beer> patchById(@PathVariable("beerId") UUID beerId,@RequestBody Beer beer){

        beerService.patchBeerById(beerId,beer);
        return new ResponseEntity<Beer>(HttpStatus.OK);

    }




}
