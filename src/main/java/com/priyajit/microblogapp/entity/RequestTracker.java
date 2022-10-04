package com.priyajit.microblogapp.entity;

import java.util.UUID;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class RequestTracker {
    String requestId;

    public static String generatedRequestId() {
        return UUID.randomUUID().toString();
    }

    public static String getCurrentRequestId() {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            return (String) attributes.getAttribute("requestId", 0);
        } else
            return null;
    }
}
