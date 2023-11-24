package ro.ctrln.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Address;
import ro.ctrln.entities.Product;
import ro.ctrln.entities.User;
import ro.ctrln.enums.Currencies;
import ro.ctrln.enums.Roles;
import ro.ctrln.repositories.ProductRepository;
import ro.ctrln.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

    public static final String LOCALHOST = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private ProductController productController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void contextLoads() {
        assertThat(productController).isNotNull();
    }

    @Test
    public void addProduct_whenUserIsAdmin_shouldStoreTheProduct() {
        productRepository.deleteAll();
        User user = getUserWithRole(Roles.ADMIN);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aProductCode");
        productDTO.setPrice(100);
        productDTO.setCurrency(Currencies.RON);
        productDTO.setStock(12);
        productDTO.setDescription("aProductDescription");
        productDTO.setValid(true);

        testRestTemplate.postForEntity(LOCALHOST + port + "/product/" + user.getId(), productDTO, Void.class);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);

        Product product = products.iterator().next();

        assertThat(product.getCode()).isEqualTo("aProductCode");
        assertThat(product.getPrice()).isEqualTo(100);
        assertThat(product.getCurrency()).isEqualTo(Currencies.RON);
    }

    @Test
    public void addProduct_whenUserIsNotAdmin_shouldNotStoreTheProduct() {
        User user = getUserWithRole(Roles.CLIENT);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aProductCode");
        productDTO.setPrice(100);
        productDTO.setCurrency(Currencies.RON);
        productDTO.setStock(12);
        productDTO.setDescription("aProductDescription");
        productDTO.setValid(true);

        ResponseEntity<String> response = testRestTemplate.postForEntity(LOCALHOST + port + "/product/" + user.getId(), productDTO, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Customer NOT allowed to add products!");
    }

    @Test
    public void addProduct_whenUserIsNotInDB_shouldNotStoreTheProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode("aProductCode");
        productDTO.setPrice(100);
        productDTO.setCurrency(Currencies.RON);
        productDTO.setStock(12);
        productDTO.setDescription("aProductDescription");
        productDTO.setValid(true);

        ResponseEntity<String> response = testRestTemplate.postForEntity(LOCALHOST + port + "/product/1000", productDTO, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Customer ID is invalid!");
    }

    private User getUserWithRole(Roles role) {
        User user = new User();
        user.setFirstname("firstname");
        user.setRoles(Collections.singletonList(role));
        user.setId(1L);
        Address address = new Address();
        address.setCity("Bucuresti");
        address.setStreet("Calea Victoriei");
        address.setNumber(1);
        address.setZipcode("123");
        user.setAddress(address);
        userRepository.save(user);
        return user;
    }
}
