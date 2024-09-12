package com.ssafy.chat.service;

import com.ssafy.chat.domain.Chat;
import com.ssafy.chat.dto.ChatHistoryResponseDto;
import com.ssafy.chat.repository.ChatRepository;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void saveChatMessage(Chat chat) throws BaseException {
        try {
            chatRepository.save(chat);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.CHAT_MESSAGE_SAVE_FAILED);
        }
    }

    /**
     * 채팅방의 history 가져오기
     *
     * @param roomId
     * @return List<Chat>
     */
    public ChatHistoryResponseDto chatHistory(String roomId) {
        ChatHistoryResponseDto response = new ChatHistoryResponseDto();
        // 이전 이력이 있는 경우, 없는 경우 모두 보내줌
        response.setHistory(chatRepository.findByRoomId(roomId));
        return response;
    }
}
