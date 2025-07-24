package com.m3h3mm3dd.trackly.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.m3h3mm3dd.trackly..*(..)) && (@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Service))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().toShortString();
        log.info("enter {}", name);
        try {
            Object result = joinPoint.proceed();
            log.info("exit {}", name);
            return result;
        } catch (Throwable ex) {
            log.error("error in {}: {}", name, ex.getMessage());
            throw ex;
        }
    }
}
