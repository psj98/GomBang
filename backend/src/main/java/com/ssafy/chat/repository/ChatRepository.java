package com.ssafy.chat.repository;

import com.ssafy.chat.domain.ChatDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<ChatDTO, String> {

}
