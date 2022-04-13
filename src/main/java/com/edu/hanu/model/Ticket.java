package com.edu.hanu.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String pnr;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "flight_id", referencedColumnName = "flight_id"),
            @JoinColumn(name = "seat_id", referencedColumnName = "seat_id")

    })
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private FlightSeatPrice flightSeatPrice;


}
