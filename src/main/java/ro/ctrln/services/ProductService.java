package ro.ctrln.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Product;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.mappers.ProductMapper;
import ro.ctrln.repositories.ProductRepository;

import java.util.Optional;

@Service
@Slf4j
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

    public void updateProduct(ProductDTO productDTO, Long customerId) throws InvalidProductCodeException {
        log.info("Customer with id {} is trying to update product {}", customerId, productDTO.getCode());
        if(productDTO.getCode() == null) {
            throw new InvalidProductCodeException();
        }

        Optional<Product> productOptional = productRepository.findByCode(productDTO.getCode());
        if(!productOptional.isPresent()) {
            throw new InvalidProductCodeException();
        }

        Product product = productOptional.get();
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCurrency(productDTO.getCurrency());
        product.setValid(productDTO.isValid());
        product.setStock(productDTO.getStock());

        productRepository.save(product);
    }
}
