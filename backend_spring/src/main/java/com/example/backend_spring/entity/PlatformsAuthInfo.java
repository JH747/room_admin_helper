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

    @ManyToOne // make it oneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @Column(length = 50, nullable = true)
    private String yapenId;

    @Column(length = 50, nullable = true)
    private String yapenPass;

    @Column(length = 50, nullable = true)
    private String yogeiId;

    @Column(length = 50, nullable = true)
    private String yogeiPass;

    @Column(length = 50, nullable = true)
    private String naverId;

    @Column(length = 50, nullable = true)
    private String naverPass;

    @Column(length = 50, nullable = true)
    private String bnbId;

    @Column(length = 50, nullable = true)
    private String bnbPass;

}
