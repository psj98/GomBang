package com.ssafy.starRoomDeal.repository;

import com.ssafy.starRoomDeal.domain.StarRoomDeal;
import com.ssafy.starRoomDeal.domain.StarRoomDealId;
import com.ssafy.starRoomDeal.domain.StarRoomDealMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StarRoomDealRepository extends JpaRepository<StarRoomDeal, StarRoomDealId> {

    Optional<List<StarRoomDealMapping>> findAllByMember_Id(UUID memberId);
}
