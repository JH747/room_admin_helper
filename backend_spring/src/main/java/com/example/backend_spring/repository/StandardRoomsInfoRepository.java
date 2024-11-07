package com.example.backend_spring.repository;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.StandardRoomsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardRoomsInfoRepository extends JpaRepository<StandardRoomsInfo, Integer> {


    StandardRoomsInfo findByRoomName(String standard_room_name);

}
