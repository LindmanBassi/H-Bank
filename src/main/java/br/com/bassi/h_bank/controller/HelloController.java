package br.com.bassi.h_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //for tests
    @GetMapping
    public ResponseEntity<String> ok(){
        System.out.println("test");
        return ResponseEntity.ok().build();
    }
}
