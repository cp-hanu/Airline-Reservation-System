package com.edu.hanu.repository;

import com.edu.hanu.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAll();
    List<Ticket> findById(long id);
}
