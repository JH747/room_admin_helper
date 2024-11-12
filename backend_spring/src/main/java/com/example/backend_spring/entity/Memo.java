package com.example.backend_spring.entity;

import jakarta.persistence.*;

@Entity
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    @Column
    private String roomTitle;

    @Column
    private String content;

}
