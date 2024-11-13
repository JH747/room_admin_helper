package com.example.backend_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PlatformsAuthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id") // foreign key column name of referencing entity's table
    @JsonIgnore
    private AppUser appUser;

    @Column(length = 32, nullable = true)
    private String yapenId;

    @Column(length = 32, nullable = true)
    private String yapenPass;

    @Column(length = 32, nullable = true)
    private String yogeiId;

    @Column(length = 32, nullable = true)
    private String yogeiPass;

    @Column(length = 32, nullable = true)
    private String naverId;

    @Column(length = 32, nullable = true)
    private String naverPass;

    @Column(length = 32, nullable = true)
    private String bnbId;

    @Column(length = 32, nullable = true)
    private String bnbPass;

    // id and pass should be checked and accepted only in pair at frontend
    @PrePersist
    @PreUpdate
    private void checkLeastTwoNotNull(){
        int notNullCount = 0;
        if(yapenId != null) notNullCount++;
        if(yogeiId != null) notNullCount++;
        if(naverId != null) notNullCount++;
        if(bnbId != null) notNullCount++;
        if(notNullCount < 2) throw new IllegalArgumentException("At least two platforms needed");
    }

}
