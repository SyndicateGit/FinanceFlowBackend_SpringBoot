package com.financeflow.financeflow_backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class TestHTTP {
    @GetMapping("/test")
    public String test() {
        return "Test HTTP";
    }
}
