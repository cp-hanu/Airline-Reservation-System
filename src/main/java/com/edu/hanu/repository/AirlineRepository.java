package com.edu.hanu.repository;

import com.edu.hanu.model.Airline;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirlineRepository extends JpaRepository<Airline,Long> {
    Airline findByFullName(String fullName);
    Airline findByAbbreviation(String abbreviation);

    @Query(value = "SELECT a FROM Airline a ORDER BY a.fullName")
    List<Airline> findAllAirline();

    Airline getById(long id);

}
