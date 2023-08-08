package com.ssafy.chat.service;

import com.ssafy.chat.domain.ChatRoom;
import com.ssafy.chat.dto.ChatCreateRequestDto;
import com.ssafy.chat.dto.ChatRoomResponseDto;
import com.ssafy.chat.dto.ChatGetIdRequestDto;
import com.ssafy.chat.repository.ChatRoomRepository;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    /**
     *
     * @param chatCreateRequestDto - 양도자 Id, 양수자 Id, 매물 Id
     * @return ChatCreateResponseDto - 채팅방 Id
     */
    public ChatRoomResponseDto createChatRoom(ChatCreateRequestDto chatCreateRequestDto) {
        ChatRoomResponseDto response = new ChatRoomResponseDto();

        UUID assignee = chatCreateRequestDto.getAssigneeId();
        UUID grantor = chatCreateRequestDto.getGrantorId();

        Optional<ChatRoom> pastRoom = chatRoomRepository.findByAssigneeIdAndGrantorId(assignee, grantor);

        if(pastRoom.isPresent()) {
            response.setRoomId(pastRoom.get().getId());
        } else {
            ChatRoom chatRoom = new ChatRoom().create(chatCreateRequestDto);
            chatRoomRepository.save(chatRoom);
            response.setRoomId(chatRoom.getId());
        }

        return response;
    }

    /**
     * 양도자, 양수자 Id로 채팅방 Id 조회
     *
     * @param chatGetIdRequestDto - 양도자 Id, 양수자 Id
     * @return ChatGetIdResponseDto - 채팅방 Id
     */
    public ChatRoomResponseDto getChatRoomId(ChatGetIdRequestDto chatGetIdRequestDto) throws BaseException {

        UUID assignee = chatGetIdRequestDto.getAssigneeId();
        UUID grantor = chatGetIdRequestDto.getGrantorId();

        Optional<ChatRoom> chatRoom = chatRoomRepository.findByAssigneeIdAndGrantorId(assignee, grantor);

        if(chatRoom.isPresent()) {
            ChatRoomResponseDto response = new ChatRoomResponseDto();
            response.setRoomId(chatRoom.get().getId());
            return response;
        } else {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_CHAT_ROOM);
        }
    }
}
