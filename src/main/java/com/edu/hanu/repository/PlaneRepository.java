package com.edu.hanu.repository;

import com.edu.hanu.model.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaneRepository extends JpaRepository<Plane,Long> {
    List<Plane> findAll();
    Plane findByName(String name);
}
