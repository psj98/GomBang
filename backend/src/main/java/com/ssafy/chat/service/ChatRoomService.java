package com.ssafy.chat.service;

import com.ssafy.chat.domain.Chat;
import com.ssafy.chat.domain.ChatRoom;
import com.ssafy.chat.dto.ChatCreateRequestDto;
import com.ssafy.chat.dto.ChatGetIdRequestDto;
import com.ssafy.chat.dto.ChatGetIdResponseDto;
import com.ssafy.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * roomID 기준으로 채팅방 찾기
     *
     * @param roomId
     * @return ChatRoom
     */
    public ChatRoom findRoomById(String roomId) {
        ChatRoom result = null;
        try {
            result = chatRoomRepository.findById(roomId).get();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * 양도자, 양수자 Id로 채팅방 Id 조회
     *
     * @param chatGetIdRequestDto
     * @return ChatGetIdResponseDto
     */
    public ChatGetIdResponseDto getChatRoomId(ChatGetIdRequestDto chatGetIdRequestDto) {

        UUID assignee = chatGetIdRequestDto.getAssigneeId();
        UUID grantor = chatGetIdRequestDto.getGrantorId();

        try {
            ChatRoom result = chatRoomRepository.findByAssigneeIdAndGrantorId(assignee, grantor).get();
            ChatGetIdResponseDto response = new ChatGetIdResponseDto();
            response.setRoomId(result.getId());
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param chatCreateRequestDto
     * @return
     * @throws Exception
     */
    public UUID createChatRoom(ChatCreateRequestDto chatCreateRequestDto) throws Exception{
        ChatRoom chatRoom = new ChatRoom().create(chatCreateRequestDto);

        UUID assignee = chatCreateRequestDto.getAssigneeId();
        UUID grantor = chatCreateRequestDto.getGrantorId();

        // 입력으로 들어온 양도자와 양수자의 id로 채팅방이 존재하는지 조회
        // 이전에 생성한 채팅방이 있지만 매물 상세보기 페이지에서 양도자와 채팅 버튼을 누른 경우
        try {
            ChatRoom dup = null;
            dup = chatRoomRepository.findByAssigneeIdAndGrantorId(assignee, grantor).get();
            return dup.getId();
        } catch (Exception e) {
            chatRoomRepository.save(chatRoom);
            return chatRoom.getId();
        }

    }
}
