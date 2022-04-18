package com.edu.hanu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "airport")
public class Airport implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id;

   private String code;
   private String name;
   private String type;
   private String country;
   private String city;

}
