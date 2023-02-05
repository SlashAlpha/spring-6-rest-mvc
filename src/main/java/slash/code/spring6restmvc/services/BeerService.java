package slash.code.spring6restmvc.services;

import slash.code.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;


public interface BeerService {

    Beer getBeerById(UUID id);

    List<Beer> listBeers();
}
