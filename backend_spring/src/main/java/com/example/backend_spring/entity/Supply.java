package com.example.backend_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "supply",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"app_user_id", "name"})
        })
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    @JsonIgnore
    private AppUser appUser;

    @Column
    private String name;

    @Column
    private int desiredQuantity; // 사용자 설정

    @Column
    private int thresholdQuantity; // 사용자 설정

    @Column
    private int currentQuantity; // 사용자 업데이트


}
