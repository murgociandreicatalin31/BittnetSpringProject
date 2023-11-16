package ro.ctrln.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class LogAspect {


    @Pointcut("execution(* ro.ctrln.controllers.ProductController.addProduct(..))")
    public void addProductPointcut() {

    }

    @Before("ro.ctrln.aspects.LogAspect.addProductPointcut()")
    public void before(JoinPoint joinPoint) {
        log.info("In before advice for aspect at {}", new Date());
        log.info("Arguments: {}", Arrays.asList(joinPoint.getArgs()));
        log.info("ProductDTO: {}", joinPoint.getArgs()[0]);
        log.info("Customer ID: {}", joinPoint.getArgs()[1]);
    }

    @After("ro.ctrln.aspects.LogAspect.addProductPointcut()")
    public void after(JoinPoint joinPoint) {
        log.info("In after advice for aspect at {}", new Date());
    }
}
