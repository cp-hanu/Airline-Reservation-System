package com.edu.hanu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

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

//   @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL)
//   Collection<Flight> fromAirport;
//
//   @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL)
//   Collection<Flight> toAirport;


//   public Airport(String code, String name) {
//      this.code = code;
//      this.name = name;
//   }
//
//   public Airport(String code) {
//      this.code = code;
//   }
//
//   public Airport(String code, String name, String type) {
//      this.code = code;
//      this.name = name;
//      this.type = type;
//   }
//
//   public Airport(String code, String name, String type, String country, String city) {
//      this.code = code;
//      this.name = name;
//      this.type = type;
//      this.country = country;
//      this.city = city;
//   }

}
