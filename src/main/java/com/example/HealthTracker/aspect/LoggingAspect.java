package com.example.HealthTracker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.HealthTracker.service.UserService.getUsers(..))")
    public void logUsers(JoinPoint jp) {
        LOGGER.info("Method: " + jp.getSignature().getName());
    }

    @After("execution(* com.example.HealthTracker.service.UserService.getUsers(..))")
    public void logMethodCallafter(JoinPoint jp){
        LOGGER.info("Method Called after " + jp.getSignature().getName());
    }

    @AfterThrowing("execution(* com.example.HealthTracker.service.UserService.getUsers(..))")
    public void logMethodCallError(JoinPoint jp){
        LOGGER.info("Method Called after error" + jp.getSignature().getName());
    }

    @AfterReturning("execution(* com.example.HealthTracker.service.UserService.getUsers(..))")
    public void logMethodCallReturn(JoinPoint jp){
        LOGGER.info("Method Called succcesfully" + jp.getSignature().getName());
    }
}
