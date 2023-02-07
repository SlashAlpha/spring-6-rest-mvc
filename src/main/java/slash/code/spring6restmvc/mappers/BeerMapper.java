package slash.code.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import slash.code.spring6restmvc.entities.Beer;
import slash.code.spring6restmvc.model.BeerDTO;

@Mapper
public interface BeerMapper {

    Beer beerDTOToBeer(BeerDTO beerDTO);
    BeerDTO beerToBeerDTO(Beer beer);
}
