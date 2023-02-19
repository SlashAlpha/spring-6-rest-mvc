package slash.code.spring6restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import slash.code.spring6restmvc.entities.BeerOrderLine;

import java.util.UUID;

public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, UUID> {


}
