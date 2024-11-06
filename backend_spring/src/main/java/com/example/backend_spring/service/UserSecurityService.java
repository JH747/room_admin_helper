package com.example.backend_spring.service;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.repository.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserSecurityService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public UserSecurityService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> _appUser = Optional.ofNullable(appUserRepository.findByUsername(username));
        if (_appUser.isEmpty()) {
            throw new UsernameNotFoundException("cannot find user: " + username);
        }
        AppUser appUser = _appUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(appUser.getUsername().equals("admin")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else{
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
