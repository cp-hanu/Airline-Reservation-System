package com.edu.hanu.repository;

import com.edu.hanu.model.Plane;
import com.edu.hanu.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByPlaneAndType(Plane plane, String type);
    List<Seat> findByPlane(Plane plane);
    Seat findSeatByRowAndColAndPlane(int row, String col, Plane plane);
    List<Seat> findAll();
}
