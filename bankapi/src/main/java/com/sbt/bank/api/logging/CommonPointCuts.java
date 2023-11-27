package com.sbt.bank.api.logging;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointCuts {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void isControllerLayer() {

    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void isServiceLayer() {

    }

    // check AOP proxy class type
    @Pointcut("this(org.springframework.data.repository.Repository)")
    public void isRepositoryLayer() {
    }
}
