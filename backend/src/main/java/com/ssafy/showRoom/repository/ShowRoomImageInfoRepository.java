package com.ssafy.showRoom.repository;

import com.ssafy.showRoom.domain.ShowRoomImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRoomImageInfoRepository extends JpaRepository<ShowRoomImageInfo, Integer> {
    List<ShowRoomImageInfo> findAllByShowRoomId(Integer id);
}
