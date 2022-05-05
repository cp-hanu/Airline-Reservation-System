package com.edu.hanu.controller;

import com.edu.hanu.model.PaypalPaymentIntent;
import com.edu.hanu.model.PaypalPaymentMethod;
import com.edu.hanu.service.GeneratePNRService;
import com.edu.hanu.service.PaypalService;
import com.edu.hanu.service.GetURLFromServer;
import com.edu.hanu.model.*;
import com.edu.hanu.repository.*;
import com.edu.hanu.service.SendEmailService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Scope("session")
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

    @Autowired
    GeneratePNRService generatePNR;

    public static final String SUCCESS_URL = "checkout/pay/success";
    public static final String CANCEL_URL = "checkout/pay/cancel";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    SendEmailService emailService;


    @GetMapping("/home")
    public String homepage(Model model) {
        List<Airline> airline = airlineRepository.findAll();
        List<Plane> planes = planeRepository.findAll();
        List<Airport> airports = airportRepository.findAll();

        String[] seats = new String[]{"FIRST CLASS", "ECONOMY CLASS", "BUSINESS CLASS"};

        model.addAttribute("flightSearch", new FlightSearch());
        model.addAttribute("airlines", airline);
        model.addAttribute("planes", planes);
        model.addAttribute("airports", airports);
        model.addAttribute("seats", seats);

        LocalDate now = LocalDate.now();
        model.addAttribute("now", now);
        return "user/index";
    }

    @PostMapping("/view")//home
    public String getFlight(Model model, FlightSearch flightSearch, BindingResult result) {
        if (result.hasErrors()) {
            return "error/500";
        }

        // one way
        if (flightSearch.getReturnDate() == null) {
            var flights = flightRepository.findByFromAirportAndToAirportAndDepartureDate(flightSearch.getFrom(), flightSearch.getTo(), flightSearch.getDepartureDate());
//            var total = flights.getClassPrice(flightSearch.getSeatClass());
            double seatTax = 0;
            double serviceTax = 0.1;
            if (flightSearch.getSeatClass().equals("FIRST CLASS")) {
                seatTax = 0.015;
            } else if (flightSearch.getSeatClass().equals("BUSINESS CLASS")) {
                seatTax = 0.03;
            } else if (flightSearch.getSeatClass().equals("ECONOMY CLASS")) {
                seatTax = 0.05;
            }
            model.addAttribute("flights", flights);
            model.addAttribute("flightSearch", flightSearch);
            model.addAttribute("tax", seatTax);
            model.addAttribute("serviceTax", serviceTax);
            return "user/view";
        }
        //round trip
        if (flightSearch.getDepartureDate() != null && flightSearch.getReturnDate() != null) {
            var flights = flightRepository.findByFromAirportAndToAirportAndDepartureDateAndArrivalDate(flightSearch.getFrom(),
                    flightSearch.getTo(), flightSearch.getDepartureDate(), flightSearch.getReturnDate());
            model.addAttribute("flights", flights);
            model.addAttribute("flightSearch", flightSearch);
            return "user/view";
        }

        return "user/index";
    }

    @GetMapping("/cart")
    public String cart(Model model){
        List<Ticket> tickets = ticketRepository.findAll();
        model.addAttribute("tickets",tickets);
        return "user/cart";
    }

    @GetMapping("/contact")
    public String contact() {
        return "user/contact";
    }

    @PostMapping("/contact")
    public String contactPost(){
        return "user/contact";
    }


    //choose flight
    @GetMapping("/view/choose")
    public String choose(@RequestParam(value = "id") long id, @RequestParam("type") String seatClass, Model model) {
        Flight flight = flightRepository.getById(id);
        // compose a array
        var test = flight.getFlightSeats().stream().filter(e -> e.getSeat().getType().equalsIgnoreCase(seatClass)).collect(Collectors.toList());


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

    @PostMapping("/payment/create")
    public String payment(HttpSession session, Model model, FlightSeat flightSeat, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "error/500";
        }

        var flightSeatPrice = flightSeatPriceRepository.findByFlightAndSeat(
                Flight.builder().id(flightSeat.getFlightId()).build(), Seat.builder().id(flightSeat.getSeatId()).build()
        );

        double tax = 0;
        double transportTax = 0.1;
        session.setAttribute("flightId", flightSeat.getFlightId());
        session.setAttribute("seatId", flightSeat.getSeatId());

        var basePrice = flightSeatPrice.getPrice();
        if(flightSeatPrice.getSeat().getType().equals("FIRST CLASS")){
            tax = 0.015;
        }else if(flightSeatPrice.getSeat().getType().equals("BUSINESS CLASS")){
            tax = 0.03;
        }else if(flightSeatPrice.getSeat().getType().equals("ECONOMY CLASS")){
            tax = 0.05;
        }
        double total = basePrice + basePrice*tax + basePrice*transportTax;
        Ticket ticket = new Ticket();
        model.addAttribute("basePrice", flightSeatPrice.getPrice());
        model.addAttribute("ticket", ticket);
        model.addAttribute("tax", tax);
        model.addAttribute("transportTax", transportTax);
        model.addAttribute("total", total);
        return "user/form-checkout";
    }

    //payment with paypal

    @GetMapping("/checkout")
    public String checkout() {
        return "user/form-checkout";
    }

    //payment with paypal


    @PostMapping("/checkout/pay")
    public String payment(HttpServletRequest request, @RequestParam("price") double price, Ticket ticket) {
        String cancelUrl = GetURLFromServer.getBaseURL(request) + "/" + CANCEL_URL;
        String successUrl = GetURLFromServer.getBaseURL(request) + "/" + SUCCESS_URL;

        request.getSession().setAttribute("ticket", ticket);
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
    public String successPay(HttpServletRequest request, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                var saveTicket = (Ticket) request.getSession().getAttribute("ticket");
                var flightId = (long) request.getSession().getAttribute("flightId");
                var seatId = (long) request.getSession().getAttribute("seatId");
                var flightSeatPrice = flightSeatPriceRepository.findByFlightAndSeat(
                        Flight.builder().id(flightId).build(), Seat.builder().id(seatId).build()
                );
                saveTicket.setPnr(generatePNR.generatePnr());
                saveTicket.setFlightSeatPrice(flightSeatPrice);
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username;
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
                saveTicket.setUser(userRepository.findByEmail(username));
                ticketRepository.save(saveTicket);
                emailService.confirmBookingNotification(saveTicket, flightRepository.getById(flightId));
                return "user/success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/checkout";
    }
}
