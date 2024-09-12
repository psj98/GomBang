package com.ssafy.roomDeal.repository;

import com.ssafy.roomDeal.domain.RoomDealImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDealImageInfoRepository extends JpaRepository<RoomDealImageInfo, Integer> {

    List<RoomDealImageInfo> findAllByRoomDealId(Long id);
}
