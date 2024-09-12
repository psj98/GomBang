package com.ssafy.showRoomHashTag.repository;

import com.ssafy.showRoomHashTag.domain.ShowRoomHashTag;
import com.ssafy.showRoomHashTag.domain.ShowRoomHashTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRoomHashTagRepository extends JpaRepository<ShowRoomHashTag, ShowRoomHashTagId> {

    @Query(value = "SELECT show_room_hash_tag.hash_tag_id FROM show_room_hash_tag WHERE show_room_hash_tag.show_room_id = ?1", nativeQuery = true)
    List<Integer> findByShowRoomId(Integer showRoomId);
}
