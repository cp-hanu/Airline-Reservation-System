package com.edu.hanu.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //    private
    @Column(name = "row_seat")
    private int row;

    @Column(name = "col_seat")
    private String col;
    private String type;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Plane plane;

}
