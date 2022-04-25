package com.edu.hanu.repository;

import com.edu.hanu.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirlineRepository extends JpaRepository<Airline,Long> {
    Airline findByFullName(String fullName);
    Airline findByAbbreviation(String abbreviation);
    List<Airline> findAll();
    Airline getById(long id);

}
