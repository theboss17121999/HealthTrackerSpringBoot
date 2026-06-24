package com.example.HealthTracker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ThreadAspect {

    @Autowired
    private ThreadPoolTaskExecutor delegateExecutor;


    @Before("@annotation(org.springframework.scheduling.annotation.Async)")
    public void logAsyncThread(JoinPoint joinPoint) {
        System.out.println(
                "Method: " + joinPoint.getSignature().toShortString()
                        + " | Thread: " + Thread.currentThread().getName()
        );
    }

    @After("@annotation(org.springframework.scheduling.annotation.Async)")
    public void logPoolStats() {

        System.out.println("------------------------------------");
        System.out.println("Current Thread : " + Thread.currentThread().getName());
        System.out.println("Active Threads: " + delegateExecutor.getActiveCount());
        System.out.println("Pool Size     : " + delegateExecutor.getPoolSize());
        System.out.println("Core Pool     : " + delegateExecutor.getCorePoolSize());
        System.out.println("Max Pool      : " + delegateExecutor.getMaxPoolSize());
        System.out.println("Queue Size    : " +
                delegateExecutor.getThreadPoolExecutor().getQueue().size());
        System.out.println("Completed     : " +
                delegateExecutor.getThreadPoolExecutor().getCompletedTaskCount());
        System.out.println("------------------------------------");
    }
}
