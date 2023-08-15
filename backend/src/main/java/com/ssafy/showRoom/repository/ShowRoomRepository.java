package com.ssafy.showRoom.repository;

import com.ssafy.showRoom.domain.ShowRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRoomRepository extends JpaRepository<ShowRoom, Integer> {
}
