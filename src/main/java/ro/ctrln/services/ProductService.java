package ro.ctrln.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Product;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.mappers.ProductMapper;
import ro.ctrln.repositories.ProductRepository;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductDTO getProduct(String productCode) throws InvalidProductCodeException {
        Optional<Product> product = productRepository.findByCode(productCode);
        if(!product.isPresent()) {
            throw new InvalidProductCodeException();
        }
        return productMapper.toDTO(product.get());
    }

    public void addProduct(ProductDTO productDTO, Long customerId) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }
}
