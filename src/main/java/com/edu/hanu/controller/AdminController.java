package com.edu.hanu.controller;

import com.edu.hanu.model.Plane;
import com.edu.hanu.model.Seat;
import com.edu.hanu.repository.PlaneRepository;
import com.edu.hanu.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(name = "/admin")
public class AdminController {
    @Autowired
    PlaneRepository planeRepository;
    @Autowired
    SeatRepository seatRepository;

    public void createPlane(int capacity, int numFirstClassRow, int numBusinessClassRow, String name, String brand) {
        Plane plane = new Plane(brand, name, capacity);
        var x = planeRepository.save(plane);

        var planeRow = x.getCapacity() / 6;
        String cols[] = {"A", "B", "C", "D", "E", "F"};
        for (int i = 0; i < planeRow; i++) {
            if (i < numFirstClassRow) {
                for (String col : cols) {
                    Seat seat = new Seat();
                    seat.setPlane(x);
                    seat.setType("FIRST CLASS");
                    seat.setCol(col);
                    seat.setRow(i);

                    seatRepository.save(seat);
                }
            } else if (i > numFirstClassRow && i < numBusinessClassRow) {
                for (String col : cols) {
                    Seat seat = new Seat();
                    seat.setPlane(x);
                    seat.setType("BUSINESS CLASS");
                    seat.setCol(col);
                    seat.setRow(i);

                    seatRepository.save(seat);
                }
            } else {
                for (String col : cols) {
                    Seat seat = new Seat();
                    seat.setPlane(x);
                    seat.setType("ECONOMY CLASS");
                    seat.setCol(col);
                    seat.setRow(i);
                    seatRepository.save(seat);
                }
            }
        }

    }
}

