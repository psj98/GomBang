package com.ssafy.chat.service;

import com.ssafy.chat.domain.Chat;
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

    /**
     *
     * @param chat
     */
    public void saveChatMessage(Chat chat) {
        chatRepository.save(chat);
    }

    /**
     * 채팅방의 history 가져오기
     *
     * @param roomId
     * @return List<Chat>
     */
    public List<Chat> chatHistory(String roomId) {
        return chatRepository.findByRoomId(roomId);
    }
}
