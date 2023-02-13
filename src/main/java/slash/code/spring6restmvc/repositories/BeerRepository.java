package slash.code.spring6restmvc.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import slash.code.spring6restmvc.entities.Beer;
import slash.code.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);
    List<Beer> findAllByBeerStyle(BeerStyle beerStyle);
    List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName,BeerStyle beerStyle);
}
