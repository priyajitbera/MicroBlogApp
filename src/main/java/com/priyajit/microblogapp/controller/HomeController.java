package com.priyajit.microblogapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/protected")
    public String homeProtected() {
        return "Home ... protected";
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to MicroblogApp";
    }
}
