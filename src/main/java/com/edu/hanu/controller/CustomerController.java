package com.edu.hanu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/customer")
public class CustomerController {

    @GetMapping("/home")
    public String homepage(){
    return "user/index";
}

    @GetMapping("/contact")
    public String contact(){
        return "user/contact";
    }
}
