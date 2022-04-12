package com.edu.hanu.repository;

import com.edu.hanu.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Flight findByFromAirportAndToAirportAndDepartureDate(long fromAirportId, long toAirportId, Date departureDate);
}
