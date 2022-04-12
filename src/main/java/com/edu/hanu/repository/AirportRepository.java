package com.edu.hanu.repository;

import com.edu.hanu.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport,Long> {
    List<Airport> findAllByNameContainingIgnoreCase(String name);

    List<Airport> findAllByCodeContainingIgnoreCase(String code);

    Airport findByName(String name);

    Airport findByNameContaining(String name);

    Airport findByCode(String code);

    List<Airport> findAll();
}
