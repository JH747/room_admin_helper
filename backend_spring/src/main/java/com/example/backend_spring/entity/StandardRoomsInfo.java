package com.example.backend_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class StandardRoomsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser app_user;

    @OneToMany(mappedBy = "standard_room_info", cascade = CascadeType.ALL, orphanRemoval = true) // foreign key field name of referencing entity
    private List<PlatformsRoomsInfo> platforms_rooms_infos = new ArrayList<>();

    @Column(length = 50, nullable = false, unique=true)
    private String room_name;

    @Column(nullable = false, unique=true)
    private Integer display_order;

    @Column(nullable = false, unique=true)
    private Integer room_quantity;

}
