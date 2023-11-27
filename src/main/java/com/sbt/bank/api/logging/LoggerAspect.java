package com.sbt.bank.api.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Around("com.sbt.bank.api.logging.CommonPointCuts.isControllerLayer()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Enter Controller: {}.{}() with argument[s] = {}", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("Exit Controller: {}.{}() with result = {}", joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(), result);
            return result;
        } catch (IllegalArgumentException e) {
            log.warn("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

    @Around("com.sbt.bank.api.logging.CommonPointCuts.isServiceLayer()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Enter Service: {}.{}() with argument[s] = {}", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("Exit Service: {}.{}() with result = {}", joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(), result);
            return result;
        } catch (IllegalArgumentException e) {
            log.warn("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

    @Around("com.sbt.bank.api.logging.CommonPointCuts.isRepositoryLayer()")
    public Object logAroundRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Enter Repository: {}.{}() with argument[s] = {}", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("Exit Repository: {}.{}() with result = {}", joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(), result);
            return result;
        } catch (IllegalArgumentException e) {
            log.warn("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
