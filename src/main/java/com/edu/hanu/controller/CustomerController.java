package com.edu.hanu.controller;

import com.edu.hanu.model.PaypalPaymentIntent;
import com.edu.hanu.model.PaypalPaymentMethod;
import com.edu.hanu.service.PaypalService;
import com.edu.hanu.service.GetURLFromServer;
import com.edu.hanu.model.*;
import com.edu.hanu.repository.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.*;
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

    @Autowired
    PaypalService service;



    @GetMapping("/home")
    public String homepage(Model model) {
        List<Airline> airline = airlineRepository.findAll();
        List<Plane> planes = planeRepository.findAll();
        List<Airport> airports = airportRepository.findAll();
//        List<Seat> seats = seatRepository.findAll();
//
//        String[] seats = new String[] {"FIRST CLASS", "ECONOMY CLASS","BUSINESS CLASS"};
//        List<String> ghe = new ArrayList<>();
//        ghe.add(seats[0]);
//        ghe.add(seats[1]);
//        ghe.add(seats[2]);

        String[] seats = new String[]{"FIRST CLASS", "ECONOMY CLASS", "BUSINESS CLASS"};
        model.addAttribute("flightSearch", new FlightSearch());
        model.addAttribute("airlines", airline);
        model.addAttribute("planes", planes);
        model.addAttribute("airports", airports);

//        model.addAttribute("seats", ghe);
        model.addAttribute("seats", seats);

        LocalDate now = LocalDate.now();
        model.addAttribute("now", now);
        return "user/index";
    }

    @PostMapping("/home")
    public String getFlight(Model model, FlightSearch flightSearch, BindingResult result) {
        if (result.hasErrors()) {
            return "error/500";
        }
        // one way
        if (flightSearch.getReturnDate() == null) {
            var flights = flightRepository.findByFromAirportAndToAirportAndDepartureDate(flightSearch.getFrom(), flightSearch.getTo(), flightSearch.getDepartureDate());

            model.addAttribute("flights", flights);
            model.addAttribute("flightSearch", flightSearch);
            return "user/view";
        }

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
        model.addAttribute("bookSeat", new FlightSeat());
        return "user/seat";
    }

    @GetMapping("/payment/create")
    public String paymentGet(Model model) {
        FlightSeat flightSeat = new FlightSeat();
        model.addAttribute("bookSeat", flightSeat);
        return "user/seat";
    }


    @PostMapping("/payment/create")
    public String payment(Model model, FlightSeat flightSeat, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "error/500";
        }
        System.out.println(flightSeat.getFlightId());

        var flightSeatPrice = flightSeatPriceRepository.findByFlightAndSeat(
                Flight.builder().id(flightSeat.getFlightId()).build(), Seat.builder().id(flightSeat.getSeatId()).build()
        );
        System.out.println(flightSeatPrice);
//        var x = flightSeatPriceRepository.findByFlightAndSeat(flight,seat);
//        System.out.println(x);
        return "user/bill";
    }

    //payment with paypal


    public static final String SUCCESS_URL = "checkout/pay/success";
    public static final String CANCEL_URL = "checkout/pay/cancel";

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/checkout")
    public String checkout() {
        return "user/form-checkout";
    }

    @PostMapping("/checkout/pay")
    public String payment(HttpServletRequest request, @RequestParam("price") double price) {
        String cancelUrl = GetURLFromServer.getBaseURL(request) + "/" + CANCEL_URL;
        String successUrl = GetURLFromServer.getBaseURL(request) + "/" + SUCCESS_URL;
        try {
            Payment payment = service.createPayment(price, "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "flight payment", cancelUrl,
                    successUrl);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/checkout";
    }
    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "user/cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "user/success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/checkout";
    }
}
