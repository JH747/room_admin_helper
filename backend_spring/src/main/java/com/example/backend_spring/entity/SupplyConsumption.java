package com.example.backend_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class SupplyConsumption {

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

    @ManyToOne
    @JoinColumn(name = "supply_id", referencedColumnName = "id")
    @JsonIgnore
    private Supply supply;

    @Column
    private int consumption;
}
