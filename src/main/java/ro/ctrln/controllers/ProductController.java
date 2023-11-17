package ro.ctrln.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.ctrln.dtos.ProductDTO;
import ro.ctrln.exceptions.InvalidProductCodeException;
import ro.ctrln.services.ProductService;

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
}
