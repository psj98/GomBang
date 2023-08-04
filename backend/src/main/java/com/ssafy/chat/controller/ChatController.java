package com.ssafy.chat.controller;

import com.ssafy.chat.domain.ChatDTO;
import com.ssafy.chat.dto.ChatEnterRequestDto;
import com.ssafy.chat.dto.ChatSendRequestDto;
import com.ssafy.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations template;

    @Autowired
    ChatRoomService service;

    // MessageMapping을 통해 webSocket으로 들어오는 메시지를 발신 처리한다.
    // 이때 클라이언트에서는 /pub/chat/message로 요청하게 되고 이것을 controller가 받아서 처리한다.
    // 처리가 완료되면 /sub/chat/room/roomId로 메시지가 전송된다.
    // userId, roomId,
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatEnterRequestDto chatEnterRequestDto, SimpMessageHeaderAccessor headerAccessor) {

        // 반환 결과를 socket session에 userUUID로 저장
        headerAccessor.getSessionAttributes().put("userID", chatEnterRequestDto.getUserId());
        headerAccessor.getSessionAttributes().put("roomId", chatEnterRequestDto.getRoomId());

//        ChatDTO chat = new ChatDTO();
//        chat.setSender(chatEnterRequestDto.getUserId());
//        chat.setType(ChatDTO.MessageType.ENTER);
//
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        chat.setTime(formatter.format(date));
//        chat.setMessage(chatEnterRequestDto.getNickName() + " 님 입장!!");
//
//        template.convertAndSend("/sub/chat/room/" + chatEnterRequestDto.getRoomId(), chat);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatSendRequestDto chatSendRequestDto) {
        ChatDTO chat = chatSendRequestDto.getChat();
        log.info("CHAT {}", chat);
        
        // 전송 시간 설정
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        chat.setTime(formatter.format(date));

        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    // 유저 퇴장 시에는 EventListener를 통해 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid와 roomId를 확인해서 채팅방 유저 리스트와 room에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);
    }
}
