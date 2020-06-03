package com.example.caching.billionaire;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "billionaires")
public class Billionaire {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String career;

}