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
}
