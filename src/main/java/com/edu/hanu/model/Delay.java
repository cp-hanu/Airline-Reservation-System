package com.edu.hanu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "delay")
public class Delay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int minute;
    private String reason;

}
