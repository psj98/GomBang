package com.ssafy.starRoomDeal.repository;

import com.ssafy.member.domain.Member;
import com.ssafy.starRoomDeal.domain.StarMemberMapping;
import com.ssafy.starRoomDeal.domain.StarRoomDeal;
import com.ssafy.starRoomDeal.domain.StarRoomDealId;
import com.ssafy.starRoomDeal.domain.StarRoomDealMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StarRoomDealRepository extends JpaRepository<StarRoomDeal, StarRoomDealId> {

    Optional<List<StarRoomDealMapping>> findAllByMemberId(UUID memberId);

    @Query("SELECT s FROM StarRoomDeal s JOIN FETCH s.member m JOIN FETCH s.roomDeal r WHERE r.id = :roomDealId")
    Optional<List<StarMemberMapping>> findAllByRoomDealId(Long roomDealId);
}
