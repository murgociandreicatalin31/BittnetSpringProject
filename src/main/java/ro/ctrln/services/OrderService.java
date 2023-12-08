package ro.ctrln.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.ctrln.dtos.OrderDTO;
import ro.ctrln.dtos.OrderProductDTO;
import ro.ctrln.entities.OnlineOrder;
import ro.ctrln.entities.OnlineOrderItem;
import ro.ctrln.entities.Product;
import ro.ctrln.entities.User;
import ro.ctrln.exceptions.*;
import ro.ctrln.repositories.OnlineOrderRepository;
import ro.ctrln.repositories.ProductRepository;
import ro.ctrln.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final StockService stockService;
    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final OnlineOrderRepository onlineOrderRepository;

    public void addOrder(OrderDTO orderDTO, Long customerId) throws InvalidProductIdException, InvalidQuantityException,
            NotEnoughStockException, InvalidProductsException, InvalidCustomerIdException {
        validateStock(orderDTO);

        Optional<User> optionalUser = userRepository.findById(customerId);
        if(!optionalUser.isPresent()) {
            throw new InvalidCustomerIdException();
        }

        OnlineOrder onlineOrder = new OnlineOrder();
        onlineOrder.setUser(optionalUser.get());
        List<OnlineOrderItem> onlineOrderItems = new ArrayList<>();

        for(OrderProductDTO orderProductDTO : orderDTO.getProducts()) {
            Optional<Product> optionalProduct = productRepository.findById(orderProductDTO.getId());
            if(optionalProduct.isPresent()) {
                Product dbProduct = optionalProduct.get();
                OnlineOrderItem onlineOrderItem = new OnlineOrderItem();
                onlineOrderItem.setQuantity(orderProductDTO.getQuantity());
                onlineOrderItem.setProduct(dbProduct);
                onlineOrder.setTotalPrice(onlineOrder.getTotalPrice() + (onlineOrderItem.getQuantity() * dbProduct.getPrice()));
                dbProduct.setStock(dbProduct.getStock() - onlineOrderItem.getQuantity());
                onlineOrderItems.add(onlineOrderItem);
            }
        }

        onlineOrder.setOrderItems(onlineOrderItems);
        onlineOrderRepository.save(onlineOrder);
    }

    public void deliverOrder(Long orderId, Long customerId) throws InvalidOrderIdException, OrderCancelledException, OrderDeliveredException {
        OnlineOrder onlineOrder = getOnlineOrder(orderId);
        if(onlineOrder.isDelivered()) {
            throw new OrderDeliveredException();
        }
        onlineOrder.setDelivered(true);
        onlineOrderRepository.save(onlineOrder);
    }

    public void cancelOrder(Long orderId, Long customerId) throws InvalidOrderIdException, OrderCancelledException, OrderDeliveredException {

        OnlineOrder onlineOrder = getOnlineOrder(orderId);
        if(onlineOrder.isDelivered()) {
            throw new OrderDeliveredException();
        }
        onlineOrder.setCancelled(true);
        onlineOrderRepository.save(onlineOrder);
    }

    public void returnOrder(Long orderId, Long customerId) throws OrderCancelledException, InvalidOrderIdException,
            OrderDeliveredException, OrderNotYetDeliveredException {

        OnlineOrder onlineOrder = getOnlineOrder(orderId);
        if(!onlineOrder.isDelivered()) {
            throw new OrderNotYetDeliveredException();
        }

        onlineOrder.setReturned(true);
        onlineOrder.getOrderItems().forEach(onlineOrderItem -> {
            Product product = onlineOrderItem.getProduct();
            product.setStock(product.getStock() + onlineOrderItem.getQuantity());
        });
        onlineOrderRepository.save(onlineOrder);
    }

    private OnlineOrder getOnlineOrder(Long orderId) throws InvalidOrderIdException, OrderCancelledException, OrderDeliveredException {
        if(orderId == null) {
            throw new InvalidOrderIdException();
        }

        OnlineOrder onlineOrder = onlineOrderRepository.findOneById(orderId);
        if(onlineOrder == null) {
            throw new InvalidOrderIdException();
        }

        if(onlineOrder.isCancelled()) {
            throw new OrderCancelledException();
        }

        return onlineOrder;
    }

    private void validateStock(OrderDTO orderDTO) throws InvalidProductIdException, InvalidQuantityException, NotEnoughStockException, InvalidProductsException {
        if(orderDTO == null || orderDTO.getProducts().isEmpty()) {
            throw new InvalidProductsException();
        }
        for(OrderProductDTO orderProductDTO : orderDTO.getProducts()) {
            if(orderProductDTO.getQuantity() < 0) {
                throw new InvalidQuantityException();
            }
            boolean havingEnoughStock = stockService.isHavingEnoughStock(orderProductDTO);
            if(!havingEnoughStock) {
                throw new NotEnoughStockException();
            }
        }
    }
}
