package com.priyajit.microblogapp.entity;

import java.util.UUID;

import org.springframework.web.context.request.RequestContextHolder;

public class RequestTracker {
    String requestId;

    public static String generatedRequestId() {
        return UUID.randomUUID().toString();
    }

    public static String getCurrentRequestId() {
        return (String) RequestContextHolder
                .getRequestAttributes().getAttribute("requestId",
                        0);
    }
}
