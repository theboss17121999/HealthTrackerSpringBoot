package com.example.HealthTracker.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionAspect {
    @AfterThrowing(
            pointcut = "execution(* com.example.HealthTracker.service..*(..))",
            throwing = "ex"
    )
    public void logException(Exception ex) {
        System.out.println("Exception occurred: " + ex.getMessage());
        ex.printStackTrace();
    }
}
