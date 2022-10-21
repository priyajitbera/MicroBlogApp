package com.priyajit.microblogapp.aspect.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.priyajit.microblogapp.entity.RequestTracker;
import com.priyajit.microblogapp.utility.ToJson;

@Aspect
@Component
public class ControllerLoggingAspect {

    Logger logger = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @AfterReturning(value = "execution(* com.priyajit.microblogapp.controller.*.*(..))", returning = "result")
    public void logReturnValue(JoinPoint joinPoint, Object result) {

        StringBuffer msg = new StringBuffer("\n");
        msg.append("requestId: " + RequestTracker.getCurrentRequestId());
        msg.append("after returning: " + joinPoint.getSignature() + "\n");
        msg.append("returned value: \n" + ToJson.toJson(result) + "\n");

        logger.info(msg.toString());
    }
}
