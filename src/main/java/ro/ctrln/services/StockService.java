package ro.ctrln.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.ctrln.dtos.OrderProductDTO;
import ro.ctrln.entities.Product;
import ro.ctrln.exceptions.InvalidProductIdException;
import ro.ctrln.repositories.ProductRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;

    public boolean isHavingEnoughStock(OrderProductDTO orderProductDTO) throws InvalidProductIdException {
        Optional<Product> optionalProduct = productRepository.findById(orderProductDTO.getId());
        if(!optionalProduct.isPresent()) {
            throw new InvalidProductIdException();
        }
        return optionalProduct.get().getStock() >= orderProductDTO.getQuantity();
    }
}
