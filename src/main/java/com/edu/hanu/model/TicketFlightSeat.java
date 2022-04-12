package com.edu.hanu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket_flight_seat")
public class TicketFlightSeat implements Serializable {
    @EmbeddedId
    private TicketFlightSeatPK pk;

    @OneToOne
    @JoinColumn(name = "ticket_id",insertable=false, updatable=false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "flight_id",insertable=false, updatable=false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "seat_id",insertable=false, updatable=false)
    private Seat seat;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class TicketFlightSeatPK implements Serializable{
        private static final long serialVersionUID = 1L;
        @Column(name = "ticket_id")
        private long ticketId;
        @Column(name = "flight_id")
        private long flightId;
        @Column(name = "seat_id")
        private long seatId;
    }

}

