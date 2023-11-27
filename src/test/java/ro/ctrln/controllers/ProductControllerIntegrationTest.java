package ro.ctrln.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void getProductByCode_whenCodeIsPresentInDB_shouldReturnTheProduct() {
        Product product = getProduct("aWonderfulCode");
        Product productTwo = getProduct("someOtherCode");
        saveProductsToDB(Arrays.asList(product, productTwo));

        ProductDTO productDTO = testRestTemplate.getForObject(LOCALHOST + port + "/product/" + product.getCode(), ProductDTO.class);

        assertThat(productDTO.getCode()).isEqualTo(product.getCode());
    }

    @Test
    public void getProductByCode_whenCodeIsNotPresentInDB_shouldReturnErrorMessage() {

        String response = testRestTemplate.getForObject(LOCALHOST + port + "/product/1234", String.class);

        assertThat(response).isEqualTo("Product code is invalid!");
    }

    @Test
    public void getProducts() {
        productRepository.deleteAll();
        Product product = getProduct("aWonderfulCode");
        Product productTwo = getProduct("someOtherCode");
        saveProductsToDB(Arrays.asList(product, productTwo));

        ResponseEntity<List<ProductDTO>> response =
                testRestTemplate.exchange(LOCALHOST + port + "/product",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProductDTO>>() {});

        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getCode()).isEqualTo("aWonderfulCode");
        assertThat(response.getBody().get(1).getCode()).isEqualTo("someOtherCode");
    }

    @Test
    public void updateProduct_whenUserIsEditor_shouldUpdateProduct() {
        Product product = getProduct("aProductEditor");
        productRepository.save(product);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode(product.getCode());
        productDTO.setCurrency(Currencies.EUR);
        productDTO.setPrice(200);
        productDTO.setStock(10);
        productDTO.setDescription("a description editor");
        productDTO.setValid(false);
        User user = getUserWithRole(Roles.EDITOR);

        testRestTemplate.put(LOCALHOST + port + "/product/" + user.getId(), productDTO);

        Optional<Product> updatedProduct = productRepository.findByCode("aProductEditor");
        assertThat(updatedProduct.isPresent()).isEqualTo(true);
        assertThat(updatedProduct.get().getCode()).isEqualTo(productDTO.getCode());
        assertThat(updatedProduct.get().getCurrency()).isEqualTo(productDTO.getCurrency());
        assertThat(updatedProduct.get().getPrice()).isEqualTo(productDTO.getPrice());
        assertThat(updatedProduct.get().getDescription()).isEqualTo(productDTO.getDescription());
        assertThat(updatedProduct.get().isValid()).isEqualTo(productDTO.isValid());

    }

    @Test
    public void updateProduct_whenUserIsAdmin_shouldUpdateProduct() {
        Product product = getProduct("aProductAdmin");
        productRepository.save(product);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode(product.getCode());
        productDTO.setCurrency(Currencies.EUR);
        productDTO.setPrice(200);
        productDTO.setStock(10);
        productDTO.setDescription("a description admin");
        productDTO.setValid(false);
        User user = getUserWithRole(Roles.ADMIN);

        testRestTemplate.put(LOCALHOST + port + "/product/" + user.getId(), productDTO);

        Optional<Product> updatedProduct = productRepository.findByCode("aProductAdmin");
        assertThat(updatedProduct.isPresent()).isEqualTo(true);
        assertThat(updatedProduct.get().getCode()).isEqualTo(productDTO.getCode());
        assertThat(updatedProduct.get().getCurrency()).isEqualTo(productDTO.getCurrency());
        assertThat(updatedProduct.get().getPrice()).isEqualTo(productDTO.getPrice());
        assertThat(updatedProduct.get().getDescription()).isEqualTo(productDTO.getDescription());
        assertThat(updatedProduct.get().isValid()).isEqualTo(productDTO.isValid());

    }

    @Test
    public void updateProduct_whenUserIsClient_shouldNotUpdateProduct() {
        Product product = getProduct("aProductClient");
        productRepository.save(product);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode(product.getCode());
        productDTO.setCurrency(Currencies.EUR);
        productDTO.setPrice(200);
        productDTO.setStock(10);
        productDTO.setDescription("a description client");
        productDTO.setValid(false);
        User user = getUserWithRole(Roles.CLIENT);

        testRestTemplate.put(LOCALHOST + port + "/product/" + user.getId(), productDTO);

        Optional<Product> updatedProduct = productRepository.findByCode("aProductClient");
        assertThat(updatedProduct.isPresent()).isEqualTo(true);
        assertThat(updatedProduct.get().getCode()).isEqualTo(product.getCode());
        assertThat(updatedProduct.get().getCurrency()).isEqualTo(product.getCurrency());
        assertThat(updatedProduct.get().getPrice()).isEqualTo(product.getPrice());
        assertThat(updatedProduct.get().getDescription()).isEqualTo(product.getDescription());
        assertThat(updatedProduct.get().isValid()).isEqualTo(product.isValid());

    }

    @Test
    public void deleteProduct_whenUserIsClient_shouldNotDeleteProduct() {
        Product product = getProduct("aProductClientDelete");
        productRepository.save(product);

        testRestTemplate.delete(LOCALHOST + port + "/product/" + product.getCode() + "/2");

        assertThat(productRepository.findByCode(product.getCode())).isPresent();
    }

    @Test
    public void deleteProduct_whenUserIsAdmin_shouldDeleteProduct() {
        Product product = getProduct("aProductAdminDelete");
        productRepository.save(product);

        testRestTemplate.delete(LOCALHOST + port + "/product/" + product.getCode() + "/1");

        assertThat(productRepository.findByCode(product.getCode())).isNotPresent();
    }

    private void saveProductsToDB(List<Product> products) {
        productRepository.saveAll(products);
    }

    private Product getProduct(String code) {
        Product product = new Product();
        product.setCode(code);
        product.setCurrency(Currencies.RON);
        product.setPrice(100);
        product.setStock(1);
        product.setDescription(code + "a description");
        product.setValid(true);
        return product;
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
