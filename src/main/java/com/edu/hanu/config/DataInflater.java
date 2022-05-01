package com.edu.hanu.config;

import com.edu.hanu.model.*;
import com.edu.hanu.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

@Configuration
@Data
public class DataInflater {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    PlaneRepository planeRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    AirlineRepository airlineRepository;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    FlightSeatPriceRepository flightSeatPriceRepository;

    @Autowired
    TicketRepository ticketRepository;


    @Transactional
    @PostConstruct
    void init() {

        // Roles
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (roleRepository.findByName("ROLE_CUSTOMER") == null) {
            roleRepository.save(new Role("ROLE_CUSTOMER"));
        }

        // Admin account
        if (userRepository.findByEmail("admin@gmail.com") == null) {
            HashSet<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_ADMIN"));
            roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
            User admin = User.builder().
                    email("admin@gmail.com")
                    .fullname("Chinh Pham")
                    .password(passwordEncoder.encode("1"))
                    .country("VN")
                    .phone("0123")
                    .roles(roles)
                    .build();
            userRepository.save(admin);
        }

        // customer account
        if (userRepository.findByEmail("customer@gmail.com") == null) {
            HashSet<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_CUSTOMER"));

            User customer = User.builder().email("customer@gmail.com").fullname("Chinh Pham")
                    .password(passwordEncoder.encode("1"))
                    .country("VN")
                    .phone("0121313")
                    .roles(roles).build();
            userRepository.save(customer);
        }

        if (!airportRepository.exists(1L)) {
            InputStream resourceStream = getClass().getResourceAsStream("/airports.json");
            try {
                ArrayList<HashMap> list = new ObjectMapper().readValue(resourceStream, ArrayList.class);
                list.forEach(o -> {
                    try {
                        Airport airport = new Airport();

                        if (o.containsKey("code")) {
                            airport.setCode(o.get("code").toString());
                        }
                        if (o.containsKey("name")) {
                            airport.setName(o.get("name").toString());
                        }
                        if (o.containsKey("type")) {
                            airport.setType(o.get("type").toString());
                        }
                        if (o.containsKey("city")) {
                            airport.setCity(o.get("city").toString());
                        }
                        if (o.containsKey("country")) {
                            airport.setCountry(o.get("country").toString());
                        }
                        if (!airport.getCity().equals("")) {
                            airportRepository.save(airport);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (!airlineRepository.exists(1L)) {
            InputStream resourceStream = getClass().getResourceAsStream("/airlines.json");
            try {
                ArrayList<HashMap> list = new ObjectMapper().readValue(resourceStream, ArrayList.class);
                list.forEach(o -> {
                    try {
                        Airline airline = new Airline();

                        if (o.containsKey("id")) {
                            airline.setAbbreviation(o.get("id").toString());
                        }
                        if (o.containsKey("name")) {
                            airline.setFullName(o.get("name").toString());
                        }
                        if (o.containsKey("logo")) {
                            airline.setLogoUrl(o.get("logo").toString());
                        }

                        airlineRepository.save(airline);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // For create plane data: ID: 1, Airbus A320
//        if (!planeRepository.exists(1L)) {
//            try {
//                Plane newPlane = Plane.builder().
//                        brand("AIRBUS")
//                        .name("A320")
//                        .capacity(180).
//                        build();
//                var plane = planeRepository.save(newPlane);
//
//                var planeRow = plane.getCapacity() / 6;
//                String[] cols = {"A", "B", "C", "D", "E", "F"};
//                for (int i = 0; i < planeRow; i++) {
//                    if (i < 3) {
//                        for (String col : cols) {
//                            Seat seat = Seat.builder()
//                                    .plane(plane)
//                                    .type("FIRST CLASS")
//                                    .col(col)
//                                    .row(i + 1)
//                                    .build();
//                            seatRepository.save(seat);
//                        }
//                    } else if (i < 6) {
//                        for (String col : cols) {
//                            Seat seat = Seat.builder()
//                                    .plane(plane)
//                                    .type("BUSINESS CLASS")
//                                    .col(col)
//                                    .row(i + 1)
//                                    .build();
//
//                            seatRepository.save(seat);
//                        }
//                    } else {
//                        for (String col : cols) {
//                            Seat seat = Seat.builder()
//                                    .plane(plane)
//                                    .type("ECONOMY CLASS")
//                                    .col(col)
//                                    .row(i + 1)
//                                    .build();
//                            seatRepository.save(seat);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        // create a new flight
//        if (!flightRepository.exists(1L)) {
//            Flight newFlight = new Flight();
//            Airline VNAirline = airlineRepository.findByAbbreviation("VN");
//
//            Airport fromAirport = airportRepository.findByCode("HAN");
//            Airport toAirport = airportRepository.findByCode("SGN");
//
//            Plane plane = planeRepository.findByName("A320");
//
//            LocalTime departureTime = LocalTime.of(5, 0);
//            LocalTime arrivalTime = LocalTime.of(7, 0);
////            Time arrivalTime = Time.valueOf("07:25:00");
//
//            Date departureDate = Date.valueOf("2022-04-20");
//            Date arrivalDate = Date.valueOf("2022-04-20");
//
//            newFlight.setFlightNo("205");
//            newFlight.setAirline(VNAirline);
//            newFlight.setPlane(plane);
//            newFlight.setFromAirport(fromAirport);
//            newFlight.setToAirport(toAirport);
//            newFlight.setDepartureDate(departureDate);
//            newFlight.setDepartureTime(departureTime);
//            newFlight.setArrivalDate(arrivalDate);
//            newFlight.setArrivalTime(arrivalTime);
//
//            var flight = flightRepository.save(newFlight);
//
//            // set price for flight
//            //
//            Collection<Seat> allSeat = plane.getSeats();
//
//            for (Seat seat : allSeat) {
//                if (seat.getType().equalsIgnoreCase("Economy Class")) {
//                    FlightSeatPrice flightSeat = FlightSeatPrice.builder()
//                            .flight(flight)
//                            .seat(seat)
//                            .price(58).build();
//                    flightSeat.setPk(new FlightSeatPrice.FlightSeatPricePk(flight.getId(), seat.getId()));
//                    flightSeatPriceRepository.save(flightSeat);
//                } else if (seat.getType().equalsIgnoreCase("BUSINESS CLASS")) {
//                    FlightSeatPrice flightSeat = FlightSeatPrice.builder()
//                            .flight(flight)
//                            .seat(seat)
//                            .price(300).build();
//                    flightSeat.setPk(new FlightSeatPrice.FlightSeatPricePk(flight.getId(), seat.getId()));
//                    flightSeatPriceRepository.save(flightSeat);
//                } else if (seat.getType().equalsIgnoreCase("First Class")) {
//                    FlightSeatPrice flightSeat = FlightSeatPrice.builder()
//                            .flight(flight)
//                            .seat(seat)
//                            .price(500).build();
//                    flightSeat.setPk(new FlightSeatPrice.FlightSeatPricePk(flight.getId(), seat.getId()));
//                    flightSeatPriceRepository.save(flightSeat);
//                }
//            }
//            // create ticket seat
//            User user = userRepository.findByEmail("customer@gmail.com");
//            Seat seat3B = seatRepository.findSeatByRowAndColAndPlane(3, "B", plane);
//
//            FlightSeatPrice flightSeatPrice = flightSeatPriceRepository.findByFlightAndSeat(flight, seat3B);
//
//            Ticket newTicket = Ticket.builder()
//                    .pnr("GWGIQR")
//                    .firstName("Nam")
//                    .lastName("Nguyen")
//                    .user(user)
//                    .flightSeatPrice(flightSeatPrice)
//                    .build();
//            ticketRepository.save(newTicket);
//
//        }

        // Get booked Seat from a flight
//        var x = flightRepository.findOne(1L);

        // Get ticket details
//        System.out.println("TEST QUERY ---------------------------------------------------");
//        var plane = planeRepository.findAll();
//        System.out.println(plane);
//        var flight = flightRepository.findAll();
//        System.out.println(flight);
//        var airline = airlineRepository.findAll();
//        System.out.println(airline);
//        System.out.println("-------------------------");
//        System.out.println(seatRepository.findAll());
//        System.out.println("-------------------------");

//        var user = userRepository.findAll();
//        System.out.println(user);
//        var ticket = ticketRepository.findOne(1L);
////        System.out.println("GET FLIGHT FROM TICKET");
////        System.out.println(ticket.getFlightSeatPrice().getFlight());
////        System.out.println("GET SEAT FROM TICKET");
////        System.out.println(ticket.getFlightSeatPrice().getSeat());
//          var
//        System.out.println("GET BOOKED SEAT");
//        var x = flightRepository.findOne(1L);
//        System.out.println(x.getFlightSeats());
//        System.out.println("--------------------------------------");

    }
}

