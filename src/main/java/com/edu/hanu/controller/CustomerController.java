package com.edu.hanu.controller;

import com.edu.hanu.model.Airline;
import com.edu.hanu.model.Airport;
import com.edu.hanu.model.Flight;
import com.edu.hanu.model.Plane;
import com.edu.hanu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    PlaneRepository planeRepository;
    @Autowired
    SeatRepository seatRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    AirlineRepository airlineRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    AirportRepository airportRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/home")
    public String homepage(Model model) {
        List<Airline> airline = airlineRepository.findAll();
        List<Plane> planes = planeRepository.findAll();
        List<Airport> airports = airportRepository.findAll();
        model.addAttribute("flight", new Flight());
        model.addAttribute("airlines", airline);
        model.addAttribute("planes", planes);
        model.addAttribute("airports", airports);
        return "user/index";
    }

    @PostMapping("/home")
    public String getFlight(Model model, Flight flight) {
        System.out.println(flight);
        return "user/view";
    }

    @GetMapping("/contact")
    public String contact() {
        return "user/contact";
    }

    @GetMapping("/view")
    public String view() {
        return "user/view";
    }

    @GetMapping("/view/detail")
    public String detail() {
        return "user/flight-detail";
    }
}
