package com.example.backend_spring.service;

import com.example.backend_spring.dto.MemoDTO;
import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.Memo;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.repository.MemoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final AppUserRepository appUserRepository;

    public MemoService(MemoRepository memoRepository, AppUserRepository appUserRepository) {
        this.memoRepository = memoRepository;
        this.appUserRepository = appUserRepository;
    }

    public boolean setMemo(MemoDTO memoDTO, String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        Memo m;
        boolean created = false;
        if(memoDTO.getId() != null){
            m = memoRepository.findById((int)memoDTO.getId());
        }
        else{
            m = new Memo();
            m.setAppUser(appUser);
            created = true;
        }
        m.setRoomTitle(memoDTO.getRoomTitle());
        m.setContent(memoDTO.getContent());
        memoRepository.save(m);
        return created;
    }
    public List<MemoDTO> getMemos(String username){
        AppUser appUser = appUserRepository.findByUsername(username);
        List<Memo> memos = memoRepository.findByAppUser(appUser);
        List<MemoDTO> memoDTOs = new ArrayList<>();
        for(Memo m : memos){
            MemoDTO mDTO = new MemoDTO();
            mDTO.setId(m.getId());
            mDTO.setRoomTitle(m.getRoomTitle());
            mDTO.setContent(m.getContent());
            memoDTOs.add(mDTO);
        }
        return memoDTOs;
    }



}
