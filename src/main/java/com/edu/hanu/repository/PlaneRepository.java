package com.edu.hanu.repository;

import com.edu.hanu.model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaneRepository extends JpaRepository<Plane,Long> {
    List<Plane> findAll();
    Plane findByName(String name);
}
