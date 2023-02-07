package slash.code.spring6restmvc.services;

import slash.code.spring6restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BeerService {

   Optional<Beer> getBeerById(UUID id);

    List<Beer> listBeers();

    Beer saveNewBeer(Beer beer);

    Beer updateBeerById(UUID beerId,Beer beer);

    void deleteById(UUID beerId);

    void patchBeerById(UUID beerId,Beer beer);


}
