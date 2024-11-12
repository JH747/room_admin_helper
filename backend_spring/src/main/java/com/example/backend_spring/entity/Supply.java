package com.example.backend_spring.entity;

import jakarta.persistence.*;

@Entity
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    @Column
    private String name;

    @Column
    private int desiredQuantity;

    @Column
    private int thresholdQuantity;

    @Column
    private int currentQuantity;


}
