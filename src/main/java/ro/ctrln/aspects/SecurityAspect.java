package ro.ctrln.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ctrln.entities.User;
import ro.ctrln.enums.Roles;
import ro.ctrln.exceptions.InvalidCustomerIdException;
import ro.ctrln.exceptions.InvalidOperationException;
import ro.ctrln.repositories.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    @Autowired
    private UserRepository userRepository;

    @Pointcut("execution(* ro.ctrln.services.ProductService.addProduct(..))")
    public void addProductPointcut() {

    }

    @Pointcut("execution(* ro.ctrln.services.ProductService.updateProduct(..))")
    public void updateProductPointcut() {

    }

    @Pointcut("execution(* ro.ctrln.services.ProductService.deleteProduct(..))")
    public void deleteProductPointcut() {

    }

    @Before("ro.ctrln.aspects.SecurityAspect.addProductPointcut()")
    public void checkSecurityBeforeAddingProduct(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long) joinPoint.getArgs()[1];
        Optional<User> userOptional = userRepository.findById(customerId);
        if(!userOptional.isPresent()) {
            throw new InvalidCustomerIdException();
        }

        User user = userOptional.get();

        if(userIsNotAllowedToAddProduct(user.getRoles())) {
            throw new InvalidOperationException("Customer NOT allowed to add products!");
        }

        log.info("Customer ID: {} will ADD the product!", customerId);
    }

    @Before("ro.ctrln.aspects.SecurityAspect.updateProductPointcut()")
    public void checkSecurityBeforeUpdatingProduct(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long) joinPoint.getArgs()[1];
        Optional<User> userOptional = userRepository.findById(customerId);
        if(!userOptional.isPresent()) {
            throw new InvalidCustomerIdException();
        }

        User user = userOptional.get();

        if(userIsNotAllowedToUpdateProduct(user.getRoles())) {
            throw new InvalidOperationException("Customer NOT allowed to update products!");
        }

        log.info("Customer ID: {} will UPDATE the product!", customerId);
    }

    @Before("ro.ctrln.aspects.SecurityAspect.deleteProductPointcut()")
    public void checkSecurityBeforeDeletingProduct(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long) joinPoint.getArgs()[1];
        Optional<User> userOptional = userRepository.findById(customerId);
        if(!userOptional.isPresent()) {
            throw new InvalidCustomerIdException();
        }

        User user = userOptional.get();

        if(userIsNotAllowedToDeleteProduct(user.getRoles())) {
            throw new InvalidOperationException("Customer NOT allowed to delete products!");
        }

        log.info("Customer ID: {} will DELETE the product!", customerId);
    }

    private boolean userIsNotAllowedToDeleteProduct(Collection<Roles> roles) {
        return !roles.contains(Roles.ADMIN);
    }

    private boolean userIsNotAllowedToUpdateProduct(Collection<Roles> roles) {
        return !roles.contains(Roles.ADMIN) && !roles.contains(Roles.EDITOR);
    }

    private boolean userIsNotAllowedToAddProduct(Collection<Roles> roles) {
        return !roles.contains(Roles.ADMIN);
    }
}
