package ro.ctrln.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{productCode}")
    public ProductDTO getProduct(@PathVariable String productCode) throws InvalidProductCodeException {
        return productService.getProduct(productCode);
    }

    @PostMapping("/{customerId}")
    public void addProduct(@RequestBody ProductDTO productDTO, @PathVariable Long customerId) {
        productService.addProduct(productDTO, customerId);
    }

    @PutMapping("/{customerId}")
    public void updateProduct(@RequestBody ProductDTO productDTO,@PathVariable Long customerId) throws InvalidProductCodeException {
        productService.updateProduct(productDTO, customerId);
    }

    @DeleteMapping("/{productCode}/{customerId}")
    public void deleteProduct(@PathVariable String productCode, @PathVariable Long customerId) throws InvalidProductCodeException {
        productService.deleteProduct(productCode, customerId);
    }

    @GetMapping
    public List<ProductDTO> getProducts() {
        return productService.getProducts();
    }
}
