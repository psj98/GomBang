package com.ssafy.chat.service;

import com.ssafy.chat.domain.ChatDTO;
import com.ssafy.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    @Autowired
    private final ChatRepository chatRepository;

    public void saveChatMessage(ChatDTO chat) {
        chatRepository.save(chat);
    }

    // 채팅방의 history 가져오기
    public List<ChatDTO> chatHistory(String roomId) {
        return chatRepository.findByRoomId(roomId);
    }
}
