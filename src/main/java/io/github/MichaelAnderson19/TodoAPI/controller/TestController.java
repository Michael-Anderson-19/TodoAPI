package io.github.MichaelAnderson19.TodoAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController()
@RequestMapping("/api")
public class TestController {

    @GetMapping
    public String test(Principal principal){
        String test = principal.getName() + " TEST ";
        return test;
    }
}
