package com.ssafy.chat.controller;

import com.ssafy.chat.domain.Chat;
import com.ssafy.chat.dto.ChatEnterRequestDto;
import com.ssafy.chat.dto.ChatSendDto;
import com.ssafy.chat.service.ChatService;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.global.common.response.ResponseService;
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
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations template;

    @Autowired
    ChatService service;

    @Autowired
    ResponseService responseService;

    /**
     *
     * MessageMapping을 통해 webSocket으로 들어오는 메시지를 발신 처리한다.
     * 이때 클라이언트에서는 /pub/chat/message로 요청하게 되고 이것을 controller가 받아서 처리한다.
     * 처리가 완료되면 /sub/chat/room/roomId로 메시지가 전송된다.
     *
     * @param chatEnterRequestDto - front에서 사용자 Id와 채팅방 Id를 JSON에 담아 보냄
     * @param headerAccessor
     * @return BaseResponse<List<Chat>> - front로 이전 채팅 이력을 List 형태로 보내줌
     */
    @MessageMapping("/chat/enteruser")
    public BaseResponse<?> enterUser(@Payload ChatEnterRequestDto chatEnterRequestDto, SimpMessageHeaderAccessor headerAccessor) {

        try {
            UUID userId = chatEnterRequestDto.getMemberId();
            UUID roomId= chatEnterRequestDto.getRoomId();

            // 반환 결과를 socket session에 userUUID로 저장
            headerAccessor.getSessionAttributes().put("userID", chatEnterRequestDto.getMemberId());
            headerAccessor.getSessionAttributes().put("roomId", chatEnterRequestDto.getRoomId());

            return responseService.getSuccessResponse(service.chatHistory(roomId.toString()));
        } catch (Exception e) {
            // Service에서 Exception을 throw 하는 경우는 없지만, 이 메서드에서 발생할 수 있는 에러를 대비
            return responseService.getFailureResponse(BaseResponseStatus.CHATROOM_CONNECT_FAIL);
        }
    }

    /**
     *
     * @param chatSendDto - Chat객체가 들어있음
     * @return BaseResponse<Chat> - 전송 성공 여부를 return  
     */
    @MessageMapping("/chat/sendmessage")
    public BaseResponse<?> sendMessage(@Payload ChatSendDto chatSendDto) {
        Chat chat = chatSendDto.getChat();
        log.info("CHAT {}", chat);

        // 전송 시간 설정
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        chat.setTime(formatter.format(date));

        // MongoDB에 채팅 메시지 저장
        try {
            service.saveChatMessage(chat);
            chatSendDto.setChat(chat);
            template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
            return responseService.getSuccessResponse(chatSendDto);
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.getStatus());
        }
    }

    /**
     * 유저 퇴장 시에는 EventListener를 통해 유저 퇴장을 확인
     *
     * @param event
     */
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
