package com.priyajit.microblogapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/protected")
    public ResponseEntity<Object> homeProtected() {
        return ResponseBuilder
                .buildResponse(null, HttpStatus.OK,
                        "This is protected URL for testing, able to access this suggest successfull authentication");
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to MicroblogApp";
    }
}
