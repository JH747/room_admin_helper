package com.example.backend_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PlatformsRoomsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "standard_room_info_id") // foreign key column name of referencing entity's table
    private StandardRoomsInfo standardRoomInfo;

    @Column(unique = true, nullable = true)
    private String yapenRoomName;

    @Column(unique = true, nullable = true)
    private String yogeiRoomName;

    @Column(unique = true, nullable = true)
    private String naverRoomName;

    @Column(unique = true, nullable = true)
    private String bnbRoomName;

}
