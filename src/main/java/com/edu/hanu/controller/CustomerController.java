package com.edu.hanu.controller;

import com.edu.hanu.model.*;
import com.edu.hanu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.SecondaryTable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        model.addAttribute("flightSearch", new FlightSearch());
        model.addAttribute("airlines", airline);
        model.addAttribute("planes", planes);
        model.addAttribute("airports", airports);
        LocalDate now = LocalDate.now();
        model.addAttribute("now", now);
        return "user/index";
    }

    @PostMapping("/home")
    public String getFlight(Model model, FlightSearch flightSearch, BindingResult result) {
        if (result.hasErrors()) {
            return "error/500";
        }
        System.out.println(flightSearch);
        // one way
        if (flightSearch.getReturnDate() == null) {
            var flights = flightRepository.findByFromAirportAndToAirportAndDepartureDate(flightSearch.getFrom(), flightSearch.getTo(), flightSearch.getDepartureDate());
            System.out.println(flightSearch.getSeatClass());
            // imagine this is economy
            model.addAttribute("flights", flights);

            Set<FlightSeatPrice> availableSeat = new HashSet<>();
            System.out.println(flights.get(0).getClassPrice("Business Class"));

            return "user/view";
        }

//        return "user/view";
        return "user/index";
    }

    @GetMapping("/contact")
    public String contact() {
        return "user/form-checkout";
    }

    @GetMapping("/flights")
    public String success() {
        return "user/success";
    }

    @GetMapping("/view")
    public String view() {
        return "user/view";
    }

    @GetMapping("/view/detail")
    public String detail() {
        return "user/flight-detail";
    }

    @GetMapping("/view/detail/fare")
    public String fare() {
        return "user/fare";
    }

    @GetMapping("/view/choose")
    public String choose() {
        return "user/choose";
    }

}
