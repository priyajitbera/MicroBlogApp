package com.priyajit.microblogapp.aspect.logging.service;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.priyajit.microblogapp.entity.RequestTracker;
import com.priyajit.microblogapp.utility.ToJson;

@Component
@Aspect
public class ServiceLoggingAspect {

    Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    /**
     * Logs all arguements provided to the methods of the service layer classes
     * 
     * @param joinPoint
     */
    // all classes in service layer
    @Before(value = "execution(* com.priyajit.microblogapp.service.*.*.*(..))")
    public void logArgs(JoinPoint joinPoint) {

        StringBuffer msg = new StringBuffer("\n");

        msg.append("requestId: " + RequestTracker.getCurrentRequestId() + "\n");
        msg.append("Before method: " + joinPoint.getSignature() + "\n");

        // appending all args to msg
        msg.append("args: \n");
        Arrays.stream(joinPoint.getArgs())
                .forEach(arg -> msg.append(ToJson.toJson(arg) + "\n"));
        logger.info(msg.toString());
    }

    /***
     * Logs the return value of all the methods of service layer classes
     * 
     * @param joinPoint
     * @param result
     */
    // all classes in service layer
    @AfterReturning(value = "execution(* com.priyajit.microblogapp.service.*.*.*(..))", returning = "result")
    public void logReturnValue(JoinPoint joinPoint, Object result) {

        StringBuffer msg = new StringBuffer("\n");
        msg.append("requestId: " + RequestTracker.getCurrentRequestId() + "\n");
        msg.append("afterReturning method: " + joinPoint.getSignature() + "\n");
        msg.append("returned value: \n" + ToJson.toJson(result) + "\n");
        logger.info(msg.toString());
    }

    /**
     * Logs exceptions thrown from of all the methods of service layer classes
     * 
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "execution(* com.priyajit.microblogapp.service.*.*.*(..))", throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) {

        StringBuffer msg = new StringBuffer("\n");
        msg.append("requestId: " + RequestTracker.generatedRequestId() + "\n");
        msg.append("AfterThrowing method: " + joinPoint.getSignature() + "\n");
        msg.append("Exception: " + e.getClass() + "\n");
        logger.error(msg.toString(), e);
    }
}
