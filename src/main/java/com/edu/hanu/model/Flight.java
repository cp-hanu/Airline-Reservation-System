package com.edu.hanu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @Column(name = "flight_no")
    private String flightNo;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "from_airport_id")
    private Airport fromAirport;

    @ManyToOne
    @JoinColumn(name = "to_airport_id")
    private Airport toAirport;

    //    @Temporal(TemporalType.DATE)
    @Column(name = "departure_date")
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private Date departureDate;

    //    @Temporal(TemporalType.TIME)
    @Column(name = "departure_time")
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss")
    private Time departureTime;

//        @Temporal(TemporalType.DATE)
    @Column(name = "arrival_date")
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private Date arrivalDate;

    //    @Temporal(TemporalType.TIME)
    @Column(name = "arrival_time")
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss")
    private Time arrivalTime;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FlightSeatPrice> flightSeats;

    @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Ticket> tickets;

//    public Set<FlightSeatPrice> getFlightSeats(){
//       Set<FlightSeatPrice> bookedSeats = new HashSet<>();
//       this.tickets.forEach(ticket -> {
//           bookedSeats.add(ticket.getFlightSeatPrice());
//       });
//
//       this.flightSeats.forEach(flightSeatPrice -> {
//          if (bookedSeats.contains(flightSeatPrice)) {
//              flightSeatPrice.setBooked(true);
//           }
//       });
//
//       return this.flightSeats;
//    }

    public TreeSet<FlightSeatPrice> getFlightSeats() {
        Set<FlightSeatPrice> bookedSeats = new HashSet<>();
        this.tickets.forEach(ticket -> {
            bookedSeats.add(ticket.getFlightSeatPrice());
        });

        Set<FlightSeatPrice> result = new HashSet<>();

        this.flightSeats.forEach(flightSeatPrice -> {
            if (bookedSeats.contains(flightSeatPrice)) {
                flightSeatPrice.setBooked(true);
                result.add(FlightSeatPrice.builder().seat(flightSeatPrice.getSeat()).isBooked(true).build());
            } else {
                result.add(FlightSeatPrice.builder().seat(flightSeatPrice.getSeat()).build());
            }
        });

        return new TreeSet<>(result);
    }

//    @ManyToMany(fetch = FetchType.EAGER)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JoinTable(
//            name = "ticket_flight_seat",
//            joinColumns = @JoinColumn(name = "flight_id"),
//            inverseJoinColumns = @JoinColumn(name = "seat_id")
//    )
//    private Set<Seat> bookedSeats;

}
