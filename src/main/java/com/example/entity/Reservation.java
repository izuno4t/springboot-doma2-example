package com.example.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class Reservation {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String name;
}
