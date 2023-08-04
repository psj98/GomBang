package com.ssafy.chat.service;

import com.ssafy.chat.domain.ChatRoom;
import com.ssafy.chat.dto.ChatCreateRequestDto;
import com.ssafy.chat.dto.ChatGetIdRequestDto;
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

    // roomID 기준으로 채팅방 찾기
    public ChatRoom findRoomById(String roomId) {
        ChatRoom result = null;
        try {
            result = chatRoomRepository.findById(roomId).get();
        } catch (Exception e) {
            System.out.println("에러남");
            return null;
        }
        return result;
    }

    // 양도자, 양수자 Id로 채팅방 Id 조회
    public ChatRoom getChatRoomId(ChatGetIdRequestDto chatGetIdRequestDto) {
        ChatRoom result = null;

        UUID assignee = chatGetIdRequestDto.getAssigneeId();
        UUID grantor = chatGetIdRequestDto.getGrantorId();

        try {
            result = chatRoomRepository.findByAssigneeIdAndGrantorId(assignee, grantor).get();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public UUID createChatRoom(ChatCreateRequestDto chatCreateRequestDto) {
        ChatRoom chatRoom = new ChatRoom().create(chatCreateRequestDto);

        UUID assignee = chatCreateRequestDto.getAssigneeId();
        UUID grantor = chatCreateRequestDto.getGrantorId();

        ChatRoom dup = null;
        
        // 입력으로 들어온 양도자와 양수자의 id로 채팅방이 존재하는지 조회
        // 이전에 생성한 채팅방이 있지만 매물 상세보기 페이지에서 양도자와 채팅 버튼을 누른 경우
        try {
            dup = chatRoomRepository.findByAssigneeIdAndGrantorId(assignee, grantor).get();
        } catch (Exception e) {
            chatRoomRepository.save(chatRoom);
            return chatRoom.getId();
        }
        return dup.getId();

    }
}
