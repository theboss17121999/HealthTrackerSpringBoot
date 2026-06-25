package com.example.HealthTracker.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {
    @Around("within(@com.example.HealthTracker.CustomAnnotations.UserServiceAnnotation *)")
    public Object measureExecutionServiceTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();

            System.out.println(
                    "Method from service layer : " + joinPoint.getSignature().toShortString() +
                            " | Time Taken: " + (end - start) + " ms"
            );
        }
    }

    @Around("within(@com.example.HealthTracker.CustomAnnotations.UserControllerAnnotation *)")
    public Object measureExecutionControllerTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();

            System.out.println(
                    "Method from Controller layer : " + joinPoint.getSignature().toShortString() +
                            " | Time Taken: " + (end - start) + " ms"
            );
        }
    }
}
