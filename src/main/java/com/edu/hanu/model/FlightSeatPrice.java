package com.edu.hanu.model;

import com.google.common.collect.ComparisonChain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flight_seat_price")
public class FlightSeatPrice implements Serializable, Comparable<FlightSeatPrice> {
    @EmbeddedId
    private FlightSeatPricePk pk;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false, insertable = false, updatable = false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false, insertable = false, updatable = false)
    private Seat seat;

    private double price;

    @Transient
    private boolean isBooked;

    @Override
    public int compareTo(FlightSeatPrice o) {
        return ComparisonChain.start()
                .compare(this.seat.getRow(), o.seat.getRow())
                .compare(this.seat.getCol(), o.seat.getCol()).result();
    }

//    public FlightSeatPrice(Seat seat, boolean isBooked){
//        this.seat = seat;
//        this.
//    }

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
