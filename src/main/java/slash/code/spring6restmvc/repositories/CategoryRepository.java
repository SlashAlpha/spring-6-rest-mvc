package slash.code.spring6restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import slash.code.spring6restmvc.entities.Category;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
