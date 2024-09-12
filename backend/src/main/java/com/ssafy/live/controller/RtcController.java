package com.ssafy.live.controller;

import com.ssafy.chat.dto.ChatCreateRequestDto;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.live.dto.RtcCreateRequestDto;
import com.ssafy.live.service.RtcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RtcController {
    private final RtcService rtcService;
    private final ResponseService responseService;

    @PostMapping("/rtc/create")
    public BaseResponse<?> createRoom(@RequestBody RtcCreateRequestDto rtcCreateRequestDto) {
        return responseService.getSuccessResponse(rtcService.createRtcRoom(rtcCreateRequestDto));
    }

    @GetMapping("/rtc/usercount/{roomId}")
    public BaseResponse<?> userCount(@PathVariable("roomId") String roomId) {
        try {
            return responseService.getSuccessResponse(rtcService.findUserCount(roomId));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.getStatus());
        }
    }
}
