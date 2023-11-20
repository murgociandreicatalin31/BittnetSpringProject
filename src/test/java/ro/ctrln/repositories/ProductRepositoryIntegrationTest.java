package ro.ctrln.repositories;


import org.junit.jupiter.api.Test;
//import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ctrln.entities.Product;
import ro.ctrln.enums.Currencies;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

//@RunWith(SpringRunner.class) // folosim @RunWith si SpringRunner ca sa rulam teste de Junit4
@DataJpaTest // When using JUnit 4, this annotation should be used in combination with @RunWith(SpringRunner.class).
public class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void findByCode_whenCodeIsPresentInDB_shouldReturnTheProduct() {
        Product productOne = new Product();
        productOne.setCode("aProductCode");
        productOne.setPrice(100);
        productOne.setStock(1);
        productOne.setValid(true);
        productOne.setCurrency(Currencies.USD);
        productOne.setDescription("A beautiful product!");

        Product productTwo = new Product();
        productOne.setCode("aProductCodeTwo");
        productOne.setPrice(100);
        productOne.setStock(1);
        productOne.setValid(true);
        productOne.setCurrency(Currencies.EUR);
        productOne.setDescription("A beautiful product two!");

        testEntityManager.persist(productOne);
        testEntityManager.persist(productTwo);
        testEntityManager.flush();

        Optional<Product> optionalProduct = productRepository.findByCode(productOne.getCode());
        assertThat(optionalProduct).isPresent();
        assertThat(optionalProduct.get().getCode()).isEqualTo(productOne.getCode());
    }

    @Test
    public void findByCode_whenCodeIsNotPresentInDB_shouldReturnEmpty() {
        Optional<Product> optionalProduct = productRepository.findByCode("some code");
        assertThat(optionalProduct).isNotPresent();
    }


}
