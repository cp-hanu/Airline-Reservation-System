package com.edu.hanu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private java.time.LocalTime departureTime;

//    private Time departureTime;

    //        @Temporal(TemporalType.DATE)
    @Column(name = "arrival_date")
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd")
    private Date arrivalDate;

    //    @Temporal(TemporalType.TIME)
    @Column(name = "arrival_time")
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private java.time.LocalTime arrivalTime;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FlightSeatPrice> flightSeats;

    @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Ticket> tickets;

    @Transient
    private Double firstClassPrice;

    @Transient
    private Double economyClassPrice;

    @Transient
    private Double businessClassPrice;


    public TreeSet<FlightSeatPrice> getFlightSeats() {
        Set<FlightSeatPrice> bookedSeats = new HashSet<>();
        this.tickets.forEach(ticket -> {
            bookedSeats.add(ticket.getFlightSeatPrice());
        });

        Set<FlightSeatPrice> result = new HashSet<>();

        this.flightSeats.forEach(flightSeatPrice -> {
            if (bookedSeats.contains(flightSeatPrice)) {
                flightSeatPrice.setBooked(true);
                result.add(flightSeatPrice);
            } else {
                result.add(flightSeatPrice);
            }
        });
        return new TreeSet<>(result);
    }

    public double getClassPrice(String seatClass) {
        return this.flightSeats.stream().filter(e -> e.getSeat().getType().equalsIgnoreCase(seatClass)).findFirst().get().getPrice();
    }

    public int getAvailableSeat(String seatClass) {
        return (int) this.getFlightSeats().stream().filter(e -> e.getSeat().getType().equalsIgnoreCase(seatClass)).count();
    }

}
