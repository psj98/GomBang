package com.ssafy.chat.controller;

import com.ssafy.chat.dto.ChatCreateRequestDto;
import com.ssafy.chat.dto.ChatGetIdRequestDto;
import com.ssafy.chat.service.ChatRoomService;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.global.common.response.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    /**
     *
     * @param chatCreateRequestDto
     * @return BaseResponse<ChatCreateResponseDto>
     */
    @PostMapping("/chatroom/create")
    public BaseResponse<?> createRoom(@RequestBody ChatCreateRequestDto chatCreateRequestDto) {
        return responseService.getSuccessResponse("채팅방 생성 성공", chatRoomService.createChatRoom(chatCreateRequestDto));
    }

    /**
     *
     * @param chatGetIdRequestDto
     * @return BaseResponse<ChatGetIdResponseDto>
     */
    @GetMapping("/chatroom/find")
    public BaseResponse<?> getChatRoomId(@RequestBody ChatGetIdRequestDto chatGetIdRequestDto) {
        try{
            return responseService.getSuccessResponse("채팅방ID 조회 성공", chatRoomService.getChatRoomId(chatGetIdRequestDto));
        } catch (BaseException e) {
            BaseResponseStatus status = e.getStatus();
            return responseService.getFailureResponse(status.getCode(), status.getMessage());
        }
    }
}
