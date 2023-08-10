package com.ssafy.chat.repository;

import com.ssafy.chat.domain.ChatRoom;
import com.ssafy.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    // 채팅방 목록 조회
    List<ChatRoom> findByAssigneeIdOrGrantorId(Member assigneeId, Member grantorId);

    // 채팅방 존재 여부 확인 & 채팅방 ID 가져오기
    Optional<ChatRoom> findByAssigneeIdAndGrantorId(Member assigneeId, Member grantorId);
}
