package com.ssafy.showRoomHashTag.repository;

import com.ssafy.showRoomHashTag.domain.ShowRoomHashTag;
import com.ssafy.showRoomHashTag.domain.ShowRoomHashTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRoomHashTagRepository extends JpaRepository<ShowRoomHashTag, ShowRoomHashTagId> {
}
