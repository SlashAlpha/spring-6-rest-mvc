package slash.code.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import slash.code.spring6restmvc.model.Beer;
import slash.code.spring6restmvc.model.BeerStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, Beer> beerMap;


    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();
        log.debug("Get Beer Id service was called. Id : ");
        Beer beer1=
                Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.ALE)
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }


    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    public Beer getBeerById(UUID id){
        return beerMap.get(id);
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        Beer savedNewBeer=
                Beer.builder()
                        .id(UUID.randomUUID())
                        .version(beer.getVersion())
                        .beerName(beer.getBeerName())
                        .beerStyle(beer.getBeerStyle())
                        .upc(beer.getUpc())
                        .price(beer.getPrice())
                        .quantityOnHand(beer.getQuantityOnHand())
                        .createdDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .build();
        beerMap.put(savedNewBeer.getId(),savedNewBeer);
        return savedNewBeer;
    }
}
