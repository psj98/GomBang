package com.ssafy.notification.controller;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.notification.dto.NotificationSendRequestDto;
import com.ssafy.notification.dto.NotificationSubscribeRequestDto;
import com.ssafy.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final ResponseService responseService;
    private final NotificationService notificationService;

    /**
     * 로그인 한 유저를 SSE에 연결한다.
     *
     * @param notificationSubscribeRequestDto 로그인 한 사용자의 아이디
     * @param lastEventId 클라이언트가 마지막으로 수신한 데이터의 id값
     * @return
     */
    @PostMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<Object> subscribe(@RequestBody NotificationSubscribeRequestDto notificationSubscribeRequestDto, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        try {
            return responseService.getSuccessResponse(notificationService.subscribe(notificationSubscribeRequestDto.getMemberId(), lastEventId));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 사용자에게 알림을 전송한다.
     * 
     * @param notificationSendRequestDto 알림을 수신할 사용자 정보, 알림 유형, 알림 내용, 연결 url 정보
     */
    @PostMapping("/send-data")
    public void sendData(@RequestBody NotificationSendRequestDto notificationSendRequestDto) {
        notificationService.send(notificationSendRequestDto.getReceiver(), notificationSendRequestDto.getNotificationType(), notificationSendRequestDto.getNotificationContent().getContent(), notificationSendRequestDto.getRelatedUrl().getUrl());
    }

    /*
    내 알림 목록 조회 기능
     */

    /*
    알림 읽음 표시 기능
     */

    /*
    알림 보내기 (사용자 리스트로 받아서 단체 알림 보내기)
     */
}
