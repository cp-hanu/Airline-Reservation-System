package com.edu.hanu.controller;

import com.edu.hanu.model.*;
import com.edu.hanu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.SecondaryTable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    FlightSeatPriceRepository flightSeatPriceRepository;

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
            // imagine this is economy
            model.addAttribute("flights", flights);

            Set<FlightSeatPrice> availableSeat = new HashSet<>();

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
    public String choose(@RequestParam(value = "id") long id, @RequestParam("type") String seatClass, Model model) {
        Flight flight = flightRepository.getById(id);
        // compose a array
        var test = flight.getFlightSeats().stream().filter(e -> e.getSeat().getType().equalsIgnoreCase(seatClass)).collect(Collectors.toList());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        System.out.println(username);

//        User user = userRepository.findByEmail("customer@gmail.com");
//        Ticket newTicket = Ticket.builder()
//                .pnr("GWGIQR")
//                .firstName("Nam")
//                .lastName("Nguyen")
//                .user(user)
//                .flightSeatPrice(test.get(4))
//                .build();
//        ticketRepository.save(newTicket);


        var x = flight.getFlightSeats().stream().filter(e -> e.getSeat().getType().equalsIgnoreCase(seatClass)).collect(Collectors.toList());
        List<List<FlightSeatPrice>> seats = new ArrayList<>();
        List<FlightSeatPrice> flightSeatPriceList = new ArrayList<>();

        for (int i = 0; i < x.size(); i++) {
            for (int j = 0; j < 6; j++) {
                flightSeatPriceList.add(x.get(i));
                i++;
            }
            i--;
            seats.add(flightSeatPriceList);
            flightSeatPriceList = new ArrayList<>();
        }
        model.addAttribute("seats", seats);
//        model.addAttribute("ticket", new Ticket());
        model.addAttribute("bookSeat", new FlightSeat());
        System.out.println(seats.get(0).get(0).getFlight().getId());
        return "user/seat";
    }

    @PostMapping("/payment/create")
    public String payment(Model model, FlightSeat flightSeat, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "error/500";
        }
        System.out.println(flightSeat.getFlightId());
//        var x = flightSeatPriceRepository.findByFlightAndSeat(flight,seat);
//        System.out.println(x);
        return "user/bill";
    }
}
