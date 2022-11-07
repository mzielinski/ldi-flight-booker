package com.maciezie.ldi.reservations.domain;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "reservation")
@Table(name = "reservations")
@Data
public class ReservationEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "flight_id")
    private Integer flightId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "passport_number")
    private String passportNumber;
}
