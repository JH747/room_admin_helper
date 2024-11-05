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
    private AppUser app_user;

    @ManyToOne
    @JoinColumn(name = "standard_room_info_id") // foreign key column name of referencing entity's table
    private StandardRoomsInfo standard_room_info;

    @Column(unique = true, nullable = false)
    private String yapen_room_name;

    @Column(unique = true, nullable = false)
    private String yogei_room_name;

    @Column(unique = true, nullable = false)
    private String naver_room_name;

    @Column(unique = true, nullable = false)
    private String bnb_room_name;

}
