package com.ssafy.roomDeal.repository;

import com.ssafy.roomDeal.domain.RoomDealOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDealOptionReposiroty extends JpaRepository<RoomDealOption, Long> {

}
