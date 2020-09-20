package com.stockinfo.controllers;

import com.stockinfo.models.TestHello;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/api", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class HelloController {
    @GetMapping(path = "/hello")
    public TestHello helloMethod() {
        return new TestHello("Wazzaaap");
    }
}
