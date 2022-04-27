package com.edu.hanu.controller;

import com.edu.hanu.model.*;
import com.edu.hanu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/internal")
public class AdminController {
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

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public String admin(Model model) {
        List<Flight> flights = flightRepository.findAll();
        List<Airline> airlines = airlineRepository.findAll();
        List<Plane> planes = planeRepository.findAll();
        List<User> users = userRepository.findAll();
        model.addAttribute("flights", flights);
        model.addAttribute("airlines", airlines);
        model.addAttribute("planes", planes);
        model.addAttribute("users", users);
        return "admin/index";
    }

    //    Planes page
    @GetMapping("/admin/planes")
    public String plane(Model model) {
        List<Plane> plane = planeRepository.findAll();
        model.addAttribute("planes", plane);
        return "admin/plane/plane";
    }

    // Create plane
    @GetMapping("/admin/planes/create")
    public String planeCreate(Model model) {
        Plane plane = new Plane();
        model.addAttribute("plane", plane);
        return "admin/plane/plane-add";
    }

    @PostMapping("/admin/planes/create")
    public String planeCreatePost(@Valid Plane plane, Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/plane/plane-add";
        }
        model.addAttribute("plane", plane);

        var newPlane = planeRepository.save(plane);

        var planeRow = newPlane.getCapacity() / 6;
        String[] cols = {"A", "B", "C", "D", "E", "F"};
        for (int i = 0; i < planeRow; i++) {
            if (i < 3) {
                for (String col : cols) {
                    Seat seat = Seat.builder()
                            .plane(newPlane)
                            .type("FIRST CLASS")
                            .col(col)
                            .row(i + 1)
                            .build();
                    seatRepository.save(seat);
                }
            } else if (i < 6) {
                for (String col : cols) {
                    Seat seat = Seat.builder()
                            .plane(newPlane)
                            .type("BUSINESS CLASS")
                            .col(col)
                            .row(i + 1)
                            .build();

                    seatRepository.save(seat);
                }
            } else {
                for (String col : cols) {
                    Seat seat = Seat.builder()
                            .plane(newPlane)
                            .type("BUSINESS CLASS")
                            .col(col)
                            .row(i + 1)
                            .build();
                    seatRepository.save(seat);
                }
            }
        }
        return "redirect:create?success";
    }

    @GetMapping("/admin/planes/update/{id}")
    public String planeUpdate(@PathVariable(value = "id") long id, Model model) {
        Plane plane = planeRepository.getById(id);
        model.addAttribute("plane", plane);
        return "admin/plane/plane-update";
    }

    @PostMapping(value = "/admin/planes/save/{id}")
    public String saveUpdate(
            @PathVariable(value = "id", required = false) long id, @Valid Plane plane,
            BindingResult result) {
        if (result.hasErrors()) {
            plane.setId(id);
            return "admin/plane/plane-update";

        }
        planeRepository.save(plane);
        return "redirect:/admin/planes";
    }

    @GetMapping(value = "/admin/planes/delete/{id}")
    public String deletePlanes(
            @PathVariable(value = "id") Long id) {
        Plane plane = planeRepository.getById(id);
        planeRepository.delete(plane);
        return "redirect:/admin/planes";
    }


    //    Flight page
    @GetMapping("/admin/flights")
    public String flight(Model model) {
        List<Flight> flights = flightRepository.findAll();
        model.addAttribute("flights", flights);
        return "admin/flight/flight";
    }

    // Create plane
    @GetMapping("/admin/flights/create")
    public String flightCreate(Model model) {
        Flight flight = new Flight();
        List<Airline> airlines = airlineRepository.findAllAirline();
        List<Plane> planes = planeRepository.findAll();
        List<Airport> airports = airportRepository.findAllAirport();
        model.addAttribute("flight", flight);
        model.addAttribute("airlines", airlines);
        model.addAttribute("planes", planes);
        model.addAttribute("airports", airports);
        model.addAttribute("dep", LocalTime.now());
        return "admin/flight/flight-add";
    }

    @PostMapping("/admin/flights/create")
    public String flightCreatePost(Flight flight, Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/flight/flight-add";
        }
        model.addAttribute("flight", flight);
        flightRepository.save(flight);
        return "redirect:?success";
    }

    @GetMapping("/admin/flights/update/{id}")
    public String flightUpdate(@PathVariable(value = "id") long id, Model model) {
        Flight flights = flightRepository.getById(id);
        List<Airline> airline = airlineRepository.findAll();
        List<Plane> planes = planeRepository.findAll();
        List<Airport> airports = airportRepository.findAll();
        model.addAttribute("flights", flights);
        model.addAttribute("airlines", airline);
        model.addAttribute("planes", planes);
        model.addAttribute("airports", airports);
        return "admin/flight/flight-update";
    }

    @PostMapping(value = "/admin/flights/save/{id}")
    public String saveFlightUpdate(
            @PathVariable(value = "id", required = false) long id, @Valid Flight flight,
            BindingResult result) {
        if (result.hasErrors()) {
            flight.setId(id);
            return "admin/flight/flight-update";
        }
        flightRepository.save(flight);
        return "redirect:/admin/flights";
    }

    @GetMapping(value = "/admin/flights/delete/{id}")
    public String deleteFlight(
            @PathVariable(value = "id") Long id) {
        Flight flight = flightRepository.getById(id);
        flightRepository.delete(flight);
        return "redirect:/admin/flights";
    }


    //    Airlines page
    @GetMapping("/admin/airlines")
    public String airline(Model model) {
        List<Airline> airline = airlineRepository.findAll();
        model.addAttribute("airlines", airline);
        return "admin/airline/airline";
    }

    @GetMapping("/admin/airlines/update/{id}")
    public String airlineUpdate(@PathVariable(value = "id") long id, Model model) {
        Airline airline = airlineRepository.getById(id);
        model.addAttribute("airline", airline);
        return "admin/airline/airline-update";
    }

    @PostMapping(value = "/admin/airlines/save/{id}")
    public String saveAirlineUpdate(
            @PathVariable(value = "id", required = false) long id, @Valid Airline airline, BindingResult result) {
        if (result.hasErrors()) {
            airline.setId(id);
            return "admin/airline/airline-update";

        }
        airlineRepository.save(airline);
        return "redirect:/admin/airlines";
    }

    @GetMapping(value = "/admin/airlines/delete/{id}")
    public String deleteAirLine(
            @PathVariable(value = "id") Long id) {
        Airline airline = airlineRepository.getById(id);
        airlineRepository.delete(airline);
        return "redirect:/admin/airlines";
    }

    //    Account page
    @GetMapping("/admin/users")
    public String user(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("user", users);
        return "admin/user/users";
    }

    @GetMapping("/admin/users/update/{id}")
    public String userUpdate(@PathVariable(value = "id") long id, Model model) {
        User user = userRepository.getById(id);
        model.addAttribute("user", user);
        return "admin/user/user-update";
    }

    @PostMapping(value = "/admin/user/save/{id}")
    public String saveUserUpdate(
            @PathVariable(value = "id", required = false) long id, @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "admin/user/user-update";

        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/admin/users/delete/{id}")
    public String deleteUser(
            @PathVariable(value = "id") Long id) {
        User user = userRepository.getById(id);
        userRepository.delete(user);
        return "redirect:/admin/users";
    }


    //View role
    @GetMapping(value = "/admin/users/roles")
    public String viewRole(Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "admin/role/roles";
    }

}

