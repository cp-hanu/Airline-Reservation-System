package com.edu.hanu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flight_seat_price")
public class FlightSeatPrice implements Serializable {
    @EmbeddedId
    private FlightSeatPricePk pk;

    @ManyToOne
    @JoinColumn(name = "flight_id",nullable = false,insertable=false, updatable=false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "seat_id",nullable = false,insertable=false, updatable=false)
    private Seat seat;

    private double price;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class FlightSeatPricePk implements Serializable {
        private static final long serialVersionUID = 1L;
        @GeneratedValue
        @Column(name = "flight_id")
        private long flightId;

        @GeneratedValue
        @Column(name = "seat_id")
        private long seatId;
    }
}
