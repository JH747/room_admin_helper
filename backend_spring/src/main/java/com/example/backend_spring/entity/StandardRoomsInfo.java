package com.example.backend_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "standard_rooms_info",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"app_user_id", "room_name"}),
                @UniqueConstraint(columnNames = {"app_user_id", "display_order"})
        })
public class StandardRoomsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    @JsonIgnore
    private AppUser appUser;

    @OneToMany(mappedBy = "standardRoomsInfo", cascade = CascadeType.ALL, orphanRemoval = true) // foreign key field name of referencing entity
    private List<PlatformsRoomsInfo> platformsRoomsInfos = new ArrayList<>();

    @Column(length = 50, nullable = false)
    private String roomName;

    @Column(nullable = false)
    private Integer roomQuantity;

    @Column(nullable = false)
    private Integer displayOrder;

}
