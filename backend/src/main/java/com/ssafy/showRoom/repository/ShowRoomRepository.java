package com.ssafy.showRoom.repository;

import com.ssafy.showRoom.domain.ShowRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRoomRepository extends JpaRepository<ShowRoom, Integer> {

    @Query(value = "SELECT * FROM show_room WHERE show_room.room_deal_id = ?1", nativeQuery = true)
    ShowRoom findByRoomDealId(Long roomDealId);
}
