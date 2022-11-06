package com.maciezie.ldi.flights.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "flight")
@Table(name = "flights")
@Data
public class FlightEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "departure_datatime")
    private Instant departureDatetime;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "arrival_datatime")
    private Instant arrivalDatetime;

}
