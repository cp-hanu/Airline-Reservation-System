package com.edu.hanu.repository;

import com.edu.hanu.model.Airport;
import com.edu.hanu.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByFromAirportAndToAirportAndDepartureDate(Airport fromAirport, Airport toAirport, Date departureDate);
    List<Flight> findByFromAirportAndToAirportAndDepartureDateAndArrivalDate(Airport fromAirport, Airport toAirport, Date departureDate,Date returnDate);
    List<Flight> findAll();
    Flight getById(long id);

    Flight getByDelayId(long id);
}
