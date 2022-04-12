package com.edu.hanu.config;

import com.edu.hanu.model.*;
import com.edu.hanu.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

@Component
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
    FlightSeatRepository flightSeatRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TicketFlightSeatRepository ticketFlightSeatRepository;

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
            User admin = new User();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setFullname("Chinh Pham");
            HashSet<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_ADMIN"));
            roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
            admin.setRoles(roles);
            userRepository.save(admin);
        }

        // customer account
        if (userRepository.findByEmail("customer@gmail.com") == null) {
            User user = new User();
            user.setEmail("customer@gmail.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setFullname("Chinh Pham");
            HashSet<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
            user.setRoles(roles);
            userRepository.save(user);
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
                        airportRepository.save(airport);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // For create plane data: ID: 1, Airbus A320
        if (!planeRepository.exists(1L)) {
            try {
                Plane newPlane = new Plane("AIRBUS", "A320", 180);
                var plane = planeRepository.save(newPlane);

                var planeRow = plane.getCapacity() / 6;
                String cols[] = {"A", "B", "C", "D", "E", "F"};
                for (int i = 0; i < planeRow; i++) {
                    if (i < 3) {
                        for (String col : cols) {
                            Seat seat = new Seat();
                            seat.setPlane(plane);
                            seat.setType("FIRST CLASS");
                            seat.setCol(col);
                            seat.setRow(i + 1);

                            seatRepository.save(seat);
                        }
                    } else if (i < 6) {
                        for (String col : cols) {
                            Seat seat = new Seat();
                            seat.setPlane(plane);
                            seat.setType("BUSINESS CLASS");
                            seat.setCol(col);
                            seat.setRow(i + 1);

                            seatRepository.save(seat);
                        }
                    } else {
                        for (String col : cols) {
                            Seat seat = new Seat();
                            seat.setPlane(plane);
                            seat.setType("ECONOMY CLASS");
                            seat.setCol(col);
                            seat.setRow(i + 1);
                            seatRepository.save(seat);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // create a new airline (VietNam Airline)
        if (!airlineRepository.exists(1L)) {
            try {
                Airline newAirline = new Airline();
                newAirline.setFullName("Vietnam Airlines");
                newAirline.setAbbreviation("VN");
                var airline = airlineRepository.save(newAirline);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // create a new flight
        if (!flightRepository.exists(1L)) {
            Flight newFlight = new Flight();
            Airline VNAirline = airlineRepository.findByAbbreviation("VN");

            Airport fromAirport = airportRepository.findByCode("HAN");
            Airport toAirport = airportRepository.findByCode("SGN");

            Plane plane = planeRepository.findByName("A320");

            Time departureTime = Time.valueOf("05:00:00");
            Time arrivalTime = Time.valueOf("07:25:00");

            Date departureDate = Date.valueOf("2022-04-20");
            Date arrivalDate = Date.valueOf("2022-04-20");

            newFlight.setFlightNo("205");
            newFlight.setAirline(VNAirline);
            newFlight.setPlane(plane);
            newFlight.setFromAirport(fromAirport);
            newFlight.setToAirport(toAirport);
            newFlight.setDepartureDate(departureDate);
            newFlight.setDepartureTime(departureTime);
            newFlight.setArrivalDate(arrivalDate);
            newFlight.setArrivalTime(arrivalTime);

            var flight = flightRepository.save(newFlight);

            // set price for flight
            //
            Collection<Seat> allSeat = plane.getSeats();

            for (Seat seat : allSeat) {
                if (seat.getType().equalsIgnoreCase("Economy Class")) {
                    FlightSeatPrice flightSeat = new FlightSeatPrice();
                    flightSeat.setFlight(flight);
                    flightSeat.setSeat(seat);
                    flightSeat.setPrice(58);
                    flightSeat.setPk(new FlightSeatPrice.FlightSeatPricePk(flight.getId(),seat.getId()));
                    flightSeatRepository.save(flightSeat);
                } else if (seat.getType().equalsIgnoreCase("BUSINESS CLASS")) {
                    FlightSeatPrice flightSeat = new FlightSeatPrice();
                    flightSeat.setFlight(flight);
                    flightSeat.setSeat(seat);
                    flightSeat.setPrice(300);
                    flightSeat.setPk(new FlightSeatPrice.FlightSeatPricePk(flight.getId(),seat.getId()));
                    flightSeatRepository.save(flightSeat);
                } else if (seat.getType().equalsIgnoreCase("First Class")) {
                    FlightSeatPrice flightSeat = new FlightSeatPrice();
                    flightSeat.setFlight(flight);
                    flightSeat.setSeat(seat);
                    flightSeat.setPrice(500);
                    flightSeat.setPk(new FlightSeatPrice.FlightSeatPricePk(flight.getId(),seat.getId()));
                    flightSeatRepository.save(flightSeat);
                }
            }
            // create ticket seat
            User user = userRepository.findByEmail("customer@gmail.com");
            Ticket newTicket = Ticket.builder()
                    .pnr("GWGIQR")
                    .firstName("Nam")
                    .lastName("Nguyen")
                    .user(user)
                    .build();
            var ticket = ticketRepository.save(newTicket);
           Seat seat3B = seatRepository.findSeatByRowAndColAndPlane(3,"B",plane) ;

           TicketFlightSeat ticketFlightSeat =
                   TicketFlightSeat.builder()
                           .seat(seat3B)
                           .flight(flight)
                           .ticket(ticket)
                           .pk(new TicketFlightSeat.TicketFlightSeatPK(ticket.getId(),flight.getId(),seat3B.getId())).build();

          ticketFlightSeatRepository.save(ticketFlightSeat);
        }

        // Get booked Seat from a flight
        var x = flightRepository.findOne(1L);
        System.out.println(x.getBookedSeats());

        // Get ticket details
       var ticket = ticketRepository.findOne(1L);
        System.out.println("GET FLIGHT FROM TICKET");
        System.out.println(ticket.getFlight().get(0));
        System.out.println("GET SEAT FROM TICKET");
        System.out.println(ticket.getSeat().get(0));

    }
}
