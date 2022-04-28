package com.edu.hanu.model;

import com.google.common.collect.ComparisonChain;
import lombok.*;

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
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
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
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private long flightId;

        @GeneratedValue
        @Column(name = "seat_id")
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private long seatId;
    }
}
