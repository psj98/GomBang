package com.ssafy.likeShowRoom.repository;

import com.ssafy.likeShowRoom.domain.LikeShowRoom;
import com.ssafy.likeShowRoom.domain.LikeShowRoomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeShowRoomRepository extends JpaRepository<LikeShowRoom, LikeShowRoomId> {

    @Query(value = "SELECT like_show_room.show_room_id FROM like_show_room WHERE like_show_room.member_id = ?1", nativeQuery = true)
    List<Integer> getMyLikeShowRoomId(UUID memberId);

    @DeleteQuery(value = "DELETE FROM like_show_room WHERE like_show_room.show_room_id = ?1")
    void deleteByShowRoomId(Integer showRoomId);
}
