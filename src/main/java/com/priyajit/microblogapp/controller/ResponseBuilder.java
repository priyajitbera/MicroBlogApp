package com.priyajit.microblogapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.priyajit.microblogapp.entity.RequestTracker;

public class ResponseBuilder {

    static Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static ResponseEntity<Object> buildResponse(Object data, HttpStatus status, String message) {

        Map<String, Object> map = new HashMap<>();

        String requestId = RequestTracker.getCurrentRequestId();

        map.put("requestId", requestId);
        map.put("status", status);
        map.put("message", message);
        map.put("data", data);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(map, status);

        return responseEntity;
    }

    public static ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {

        Map<String, Object> map = new HashMap<>();

        String requestId = RequestTracker.getCurrentRequestId();

        map.put("requestId", requestId);
        map.put("status", status);
        map.put("message", message);
        // map.put("processingEndDate", new Date());
        return new ResponseEntity<>(map, status);
    }
}
