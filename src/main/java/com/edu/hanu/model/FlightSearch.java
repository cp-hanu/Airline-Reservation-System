package com.edu.hanu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightSearch {
    Airport from;
    Airport to;
    Date departureDate;
    Date returnDate;
}
