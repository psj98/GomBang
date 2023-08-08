package com.ssafy.chat.repository;

import com.ssafy.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    // 이전 기록 가져오기 용
    public List<Chat> findByRoomId(String roomId);
}
