package com.edu.hanu.repository;

import com.edu.hanu.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline,Long> {
    Airline findByFullName(String fullName);
    Airline findByAbbreviation(String abbreviation);
}
