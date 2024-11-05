package com.example.backend_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PlatformsAuthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser app_user;

    @Column(length = 50, nullable = true)
    private String yapen_id;
    @Column(length = 50, nullable = true)
    private String yapen_pass;
    @Column(length = 50, nullable = true)
    private String yogei_id;
    @Column(length = 50, nullable = true)
    private String yogi_pass;
    @Column(length = 50, nullable = true)
    private String naver_id;
    @Column(length = 50, nullable = true)
    private String naver_pass;
    @Column(length = 50, nullable = true)
    private String bnb_id;
    @Column(length = 50, nullable = true)
    private String bnb_pass;

}
