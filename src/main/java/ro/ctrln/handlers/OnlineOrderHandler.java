package ro.ctrln.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.ctrln.exceptions.*;

@ControllerAdvice
public class OnlineOrderHandler {

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<String> handleInvalidProductIdException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID is invalid!");
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> handleInvalidQuantityException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The quantity of a product must be positive!");
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<String> handleNotEnoughStockException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One of the products is out of stock!");
    }

    @ExceptionHandler(InvalidProductsException.class)
    public ResponseEntity<String> handleInvalidProductsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The order does not contain any products!");
    }

    @ExceptionHandler(InvalidOrderIdException.class)
    public ResponseEntity<String> handleInvalidOrderIdException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No valid order ID has been transmitted!");
    }

    @ExceptionHandler(OrderCancelledException.class)
    public ResponseEntity<String> handleOrderCancelledException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The order has already been cancelled!");
    }

    @ExceptionHandler(OrderDeliveredException.class)
    public ResponseEntity<String> handleOrderDeliveredException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The order has already been delivered!");
    }
}
