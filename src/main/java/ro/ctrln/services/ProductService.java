package ro.ctrln.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Product;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.mappers.ProductMapper;
import ro.ctrln.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ProductMapper productMapper;

    public void addProduct(ProductDTO productDTO, Long customerId) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }

    public ProductDTO getProduct(String productCode) throws InvalidProductCodeException {
        Product product = getProductEntity(productCode);
        return productMapper.toDTO(product);
    }

    public void updateProduct(ProductDTO productDTO, Long customerId) throws InvalidProductCodeException {
        log.info("Customer with id {} is trying to update product {}", customerId, productDTO.getCode());
        if (productDTO.getCode() == null) {
            throw new InvalidProductCodeException();
        }

        Product product = getProductEntity(productDTO.getCode());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCurrency(productDTO.getCurrency());
        product.setValid(productDTO.isValid());
        product.setStock(productDTO.getStock());

        productRepository.save(product);
    }

    public void deleteProduct(String productCode, Long customerId) throws InvalidProductCodeException {
        log.info("Customer with id {} is trying to delete product {}", customerId, productCode);
        if (productCode == null) {
            throw new InvalidProductCodeException();
        }
        Product product = getProductEntity(productCode);
        productRepository.delete(product);
    }

    private Product getProductEntity(String productCode) throws InvalidProductCodeException {
        Optional<Product> product = productRepository.findByCode(productCode);
        if (!product.isPresent()) {
            throw new InvalidProductCodeException();
        }
        return product.get();
    }

    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream().map(productMapper::toDTO).collect(Collectors.toList());
    }
}
