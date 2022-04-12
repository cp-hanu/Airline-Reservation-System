package com.edu.hanu.repository;

import com.edu.hanu.model.FlightSeatPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightSeatRepository extends JpaRepository<FlightSeatPrice, Long> {

}
