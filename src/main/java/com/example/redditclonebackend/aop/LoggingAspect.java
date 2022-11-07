package com.example.redditclonebackend.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut(value = "execution(* com.example.redditclonebackend.controller.*.*(..))")
    private void forControllerPackage() {}

    @Pointcut(value = "execution(* com.example.redditclonebackend.repository.*.*(..))")
    private void forRepositoryPackage() {}

    @Pointcut(value = "execution(* com.example.redditclonebackend.service.*.*(..))")
    private void forServicePackage() {}

    @Pointcut(value = "forControllerPackage() || forRepositoryPackage() || forServicePackage()")
    private void forAppFlow() {}

    @Before(value = "forAppFlow()")
    public void logBeforeEachMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Called method " + methodName);

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            log.info("Method argument:" + arg);
        }
    }

    @AfterReturning(pointcut = "forAppFlow()", returning = "result")
    public void loggingAfterEachMethodReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();

        log.info("After calling method: " + methodName);
        log.info("Result from calling method: " + methodName + " is: " + result);
    }
}
