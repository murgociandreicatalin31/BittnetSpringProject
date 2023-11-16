package ro.ctrln.mappers;

import org.springframework.stereotype.Component;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.entities.Product;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if(product == null) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setCode(product.getCode());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setValid(product.isValid());
        productDTO.setCurrency(product.getCurrency());
        return productDTO;
    }

    public Product toEntity(ProductDTO productDTO) {
        if(productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setCode(productDTO.getCode());
        product.setId(productDTO.getId());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setValid(productDTO.isValid());
        product.setCurrency(productDTO.getCurrency());
        product.setStock(productDTO.getStock());
        return product;
    }
}
