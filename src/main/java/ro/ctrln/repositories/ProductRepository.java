package ro.ctrln.repositories;

import org.springframework.data.repository.CrudRepository;
import ro.ctrln.entities.Product;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByCode(String productCode);
}
