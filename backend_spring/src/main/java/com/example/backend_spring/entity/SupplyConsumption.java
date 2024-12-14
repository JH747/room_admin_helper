package com.example.backend_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "supply_consumption",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"app_user_id", "standard_room_info_id", "supply_id"})
        })
public class SupplyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    @JsonIgnore
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "standard_room_info_id", referencedColumnName = "id")
    @JsonIgnore
    private StandardRoomsInfo standardRoomsInfo;

    @ManyToOne
    @JoinColumn(name = "supply_id", referencedColumnName = "id")
    @JsonIgnore
    private Supply supply;

    @Column
    private int consumption;
}
