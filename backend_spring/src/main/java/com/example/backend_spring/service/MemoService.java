package com.example.backend_spring.service;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.Memo;
import com.example.backend_spring.entity.PlatformsAuthInfo;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.repository.MemoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final AppUserRepository appUserRepository;

    public MemoService(MemoRepository memoRepository, AppUserRepository appUserRepository) {
        this.memoRepository = memoRepository;
        this.appUserRepository = appUserRepository;
    }

    public Memo createMemo(Memo memo, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        memo.setAppUser(appUser);
        memoRepository.save(memo);
        return memo;
    }

    public List<Memo> getMemos(String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        return memoRepository.findByAppUser(appUser);
    }


}
