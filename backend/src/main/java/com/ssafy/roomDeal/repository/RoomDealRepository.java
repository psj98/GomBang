package com.ssafy.roomDeal.repository;

import com.ssafy.member.domain.Member;
import com.ssafy.roomDeal.domain.RoomDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDealRepository extends JpaRepository<RoomDeal, Long> {
    List<RoomDeal> findAllByMember(Member member);
}
