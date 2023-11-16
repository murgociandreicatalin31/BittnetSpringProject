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

    @Before("ro.ctrln.aspects.SecurityAspect.addProductPointcut()")
    public void checkSecurityBeforeAddingProduct(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long customerId = (Long) joinPoint.getArgs()[1];
        Optional<User> userOptional = userRepository.findById(customerId);
        if(!userOptional.isPresent()) {
            throw new InvalidCustomerIdException();
        }

        User user = userOptional.get();

        if(userIsNotAllowedToAddProduct(user.getRoles())) {
            throw new InvalidOperationException();
        }

        log.info("Customer ID: {} will modify the product!", customerId);
    }

    private boolean userIsNotAllowedToAddProduct(Collection<Roles> roles) {
        return !roles.contains(Roles.ADMIN);
    }
}
