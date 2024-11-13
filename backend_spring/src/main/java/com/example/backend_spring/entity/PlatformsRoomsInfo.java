package com.example.backend_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "standard_room_info_id", referencedColumnName = "id")
    @JsonIgnore
    private StandardRoomsInfo standardRoomsInfo;

    @Column(nullable = true)
    private String yapenRoomName;

    @Column(nullable = true)
    private String yogeiRoomName;

    @Column(nullable = true)
    private String naverRoomName;

    @Column(nullable = true)
    private String bnbRoomName;

    @Column(nullable = false)
    private Integer displayOrder;

    @PrePersist
    @PreUpdate
    private void checkLeastTwoNotNull(){
        int notNullCount = 0;
        if(yapenRoomName != null) notNullCount++;
        if(yogeiRoomName != null) notNullCount++;
        if(naverRoomName != null) notNullCount++;
        if(bnbRoomName != null) notNullCount++;
        if(notNullCount < 2) throw new IllegalArgumentException("At least two platforms needed");
    }

}
