package ro.ctrln.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ro.ctrln.mappers.ProductMapper;
import ro.ctrln.repositories.ProductRepository;

//@RunWith(SpringRunner.class)
public class ProductServiceMocksTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void contextLoads() {

    }
}
