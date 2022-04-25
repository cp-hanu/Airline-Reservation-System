package com.edu.hanu.repository;

import com.edu.hanu.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Flight findByFromAirportAndToAirportAndDepartureDate(long fromAirportId, long toAirportId, Date departureDate);
    List<Flight> findAll();
    Flight getById(long id);
}
