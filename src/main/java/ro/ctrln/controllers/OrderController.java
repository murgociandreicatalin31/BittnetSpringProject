package ro.ctrln.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.ctrln.dtos.OrderDTO;
import ro.ctrln.exceptions.*;
import ro.ctrln.services.OrderService;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{customerId}")
    public void addOrder(@RequestBody OrderDTO orderDTO, @PathVariable Long customerId) throws InvalidProductIdException,
            InvalidQuantityException, NotEnoughStockException, InvalidProductsException, InvalidCustomerIdException {
        orderService.addOrder(orderDTO, customerId);
    }

    @PatchMapping("/{orderId}/{customerId}")
    public void deliver(@PathVariable Long orderId, @PathVariable Long customerId) throws InvalidOrderIdException, OrderCancelledException, OrderDeliveredException {
        orderService.deliverOrder(orderId, customerId);
    }
}
