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
    private AppUser appUser;

    @OneToMany(mappedBy = "standardRoomInfo", cascade = CascadeType.ALL, orphanRemoval = true) // foreign key field name of referencing entity
    private List<PlatformsRoomsInfo> platformsRoomsInfos = new ArrayList<>();

    @Column(length = 50, nullable = false, unique=true)
    private String roomName;

    @Column(nullable = false, unique=true)
    private Integer displayOrder;

    @Column(nullable = false, unique=true)
    private Integer roomQuantity;

}
