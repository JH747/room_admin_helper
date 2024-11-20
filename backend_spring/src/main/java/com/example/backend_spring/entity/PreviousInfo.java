package com.example.backend_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class PreviousInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    @JsonIgnore
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "standard_rooms_info_id", referencedColumnName = "id")
    @JsonIgnore
    private StandardRoomsInfo standardRoomsInfo;

    @Column
    private LocalDate date;

    @Column
    private int yapenBooked;

    @Column
    private int yogeiBooked;

    @Column
    private int naverBooked;

    @Column
    private int bnbBooked;
}
