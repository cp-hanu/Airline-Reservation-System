package com.edu.hanu.repository;

import com.edu.hanu.model.Delay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelayRepository extends JpaRepository<Delay, Long> {
    Delay getDelayById(long id);
}
