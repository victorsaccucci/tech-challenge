package com.fiap.zecomanda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "address")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String neighborhood;
    private String city;
    private String number;
    private String state;
    private String country;
}
