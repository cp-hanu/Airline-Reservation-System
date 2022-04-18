package com.edu.hanu.repository;

import com.edu.hanu.model.Flight;
import com.edu.hanu.model.FlightSeatPrice;
import com.edu.hanu.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightSeatPriceRepository extends JpaRepository<FlightSeatPrice, Long> {

    FlightSeatPrice findByFlightAndSeat(Flight flight, Seat seat);

}
