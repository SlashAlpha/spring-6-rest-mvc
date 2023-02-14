package slash.code.spring6restmvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import slash.code.spring6restmvc.model.BeerDTO;
import slash.code.spring6restmvc.model.BeerStyle;
import slash.code.spring6restmvc.services.BeerService;

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

    @GetMapping
    public Page<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false) BeerStyle beerStyle,
                                   @RequestParam(required = false) Boolean showInventory,
                                   @RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize){
        return beerService.listBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
    }

    @RequestMapping(value = BEER_PATH_PARAM,method = RequestMethod.GET)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId){
        log.debug("GetBeer By Id in Beer Controller");
       return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BeerDTO> handlePost(@Validated @RequestBody BeerDTO newBeer){

        HttpHeaders headers= new HttpHeaders();

      BeerDTO savedBeer=  beerService.saveNewBeer(newBeer);
        headers.add("Location",BEER_URI+"/"+savedBeer.getId().toString());
       return new ResponseEntity<BeerDTO>(headers,HttpStatus.CREATED);

    }

    @PutMapping(BEER_PATH_PARAM)
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId,@Validated @RequestBody BeerDTO beer){

        if (beerService.updateBeerById(beerId,beer).isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.OK);

    }

    @DeleteMapping(BEER_PATH_PARAM)
    public  ResponseEntity<BeerDTO> deleteById(@PathVariable("beerId")UUID beerId){
        ;
        if (!beerService.deleteById(beerId)){
            throw new NotFoundException();
        }

        return new ResponseEntity<BeerDTO>(HttpStatus.NO_CONTENT);

    }
    @PatchMapping(BEER_PATH_PARAM)
    public ResponseEntity<BeerDTO> patchById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer){

        if (beerService.patchBeerById(beerId,beer).isEmpty()){
            throw new NotFoundException();
        }

        beerService.patchBeerById(beerId,beer);
        return new ResponseEntity<BeerDTO>(HttpStatus.OK);

    }





}
