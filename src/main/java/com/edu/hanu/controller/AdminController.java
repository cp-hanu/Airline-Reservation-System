package com.edu.hanu.controller;

import com.edu.hanu.model.Plane;
import com.edu.hanu.model.Seat;
import com.edu.hanu.repository.PlaneRepository;
import com.edu.hanu.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@RequestMapping(name = "/admin")
public class AdminController {
    @Autowired
    PlaneRepository planeRepository;
    @Autowired
    SeatRepository seatRepository;

//    @GetMapping("/admin")
//    public String homepageAdmin(){
//        return "user/index2";
//    }

}

