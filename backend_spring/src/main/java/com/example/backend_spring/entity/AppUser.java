package com.example.backend_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "app_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlatformsAuthInfo> platforms_auth_infos = new ArrayList<>();

    @OneToMany(mappedBy = "app_user", cascade = CascadeType.ALL, orphanRemoval = true) // foreign key field name of referencing entity
    private List<StandardRoomsInfo> standard_rooms_infos = new ArrayList<>();

    @OneToMany(mappedBy = "app_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlatformsRoomsInfo> platforms_rooms_infos = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String emil;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
