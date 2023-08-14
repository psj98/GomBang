package com.ssafy.notification.controller;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.notification.dto.NotificationSendRequestDto;
import com.ssafy.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final ResponseService responseService;
    private final NotificationService notificationService;

    /**
     * 로그인 한 유저를 SSE에 연결한다.
     *
     * @param memberId 로그인 한 사용자의 아이디
     * @param lastEventId 클라이언트가 마지막으로 수신한 데이터의 id값
     * @return emitter
     */
    @GetMapping(value = "/subscribe/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable UUID memberId, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(memberId, lastEventId);
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

    /**
     * 알림을 읽음 표시한다.
     *
     * @param id 읽음 처리할 알림의 고유 아이디
     * @retrurn 읽음 처리한 알림 객체
     */
    @GetMapping("/read/{id}")
    public BaseResponse<Object> readNotification(@PathVariable Long id) {
        try {
            return responseService.getSuccessResponse(notificationService.changeIsReadToTrue(id));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 사용자가 받은 모든 알림을 조회한다.
     *
     * @param memberId 사용자의 UUID
     * @return 사용자가 받은 전체 알림 리스트
     */
    @GetMapping("/list/{memberId}")
    public BaseResponse<Object> getNotificationList(@PathVariable UUID memberId) {
        try {
            return responseService.getSuccessResponse(notificationService.searchByMemberId(memberId));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

}
