package com.revshop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // Matches all methods in any class within com.revshop.service package
    @Before("execution(* com.revshop.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[AOP] Entering method: {} with args: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.revshop.service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[AOP] Exiting method: {} with result: {}",
                joinPoint.getSignature().getName(),
                result);
    }
}
