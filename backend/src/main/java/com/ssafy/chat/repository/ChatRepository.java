package com.ssafy.chat.repository;

import com.ssafy.chat.domain.ChatDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends MongoRepository<ChatDTO, String> {
    // 이전 기록 가져오기 용
    public List<ChatDTO> findByRoomId(String roomId);
}
