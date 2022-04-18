package com.edu.hanu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/customer")
public class CustomerController {

    @GetMapping("/home")
    public String home(){
        return "user/home";
    }
}
