package com.ssafy.chat.controller;

import com.ssafy.chat.domain.ChatRoom;
import com.ssafy.chat.dto.ChatCreateRequestDto;
import com.ssafy.chat.dto.ChatCreateResponseDto;
import com.ssafy.chat.dto.ChatGetIdRequestDto;
import com.ssafy.chat.service.ChatRoomService;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class ChatRoomController {
    // ChatService Bean 가져오기
    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ResponseService responseService;

    // 채팅방 생성
    // 채팅을 보내면 상대를 채팅방으로 초대
    // 메시지 보내기 -> 방 생성 -> 방으로 이동
    // 초대를 수락하면 -> 방 이동
    @PostMapping("/chat/createroom")
    public BaseResponse<?> createRoom(@RequestBody ChatCreateRequestDto chatCreateRequestDto) {
        // 방 생성
        UUID roomId = chatRoomService.createChatRoom(chatCreateRequestDto);
        log.info("Create chat roomID {}", roomId);

        ChatCreateResponseDto response = new ChatCreateResponseDto();
        response.setRoomId(roomId);

        return responseService.getSuccessResponse("create success", response);
    }

    @GetMapping("/chat/findchatroom")
    public BaseResponse<?> getChatRoomId(@RequestBody ChatGetIdRequestDto chatGetIdRequestDto) {
        ChatRoom resultChatRoom = chatRoomService.getChatRoomId(chatGetIdRequestDto);
        if(resultChatRoom == null) return responseService.getFailureResponse("Not Found");
        return responseService.getSuccessResponse("find chatRoomId success", resultChatRoom.getId());
    }

    // 채팅방 입장 화면
    // 재입장시 과거 메세지 가져오기?
    // NoSQL 적용하면 그때 만들자
//    @GetMapping("/chat/room/{roomId}")
//    public BaseResponse<?> roomDetail(@PathVariable("roomId") String roomId) {
//        log.info("roomId {}", roomId);
//        ChatRoom room = chatService.findRoomById(roomId);
//
//        return responseService.getSuccessResponse("enter success", room);
//    }
}
