package com.ssafy.notification.service;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.member.domain.Member;
import com.ssafy.notification.domain.Notification;
import com.ssafy.notification.domain.NotificationType;
import com.ssafy.notification.dto.NotificationResponseDto;
import com.ssafy.notification.dto.NotificationSubscribeResponseDto;
import com.ssafy.notification.repository.EmitterRepositoryImpl;
import com.ssafy.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepositoryImpl emitterRepositoryImpl;
    private final NotificationRepository notificationRepository;

    /*
    SSE 연결
     */
    /**
     * 클라이언트가 구독을 위해 호출하는 메서드
     *
     * @param memberId 구독하는 클라이언트의 사용자 아이디
     * @return SseEmitter 서버에서 보낸 이벤트 Emitter
     */
    public NotificationSubscribeResponseDto subscribe(UUID memberId, String lastEventId) throws BaseException {
        // memberId가 입력되지 않았을 경우 예외처리
        if (memberId == null)
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID);

        String emitterId = createIdWithMemberId(memberId);
        SseEmitter emitter = emitterRepositoryImpl.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        // 네트워크 오류로 비동기 요청이 정상 동작할 수 없을 때 Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepositoryImpl.deleteById(emitterId));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepositoryImpl.deleteById(emitterId));

        // 503에러를 방지하기 위한 더미 이벤트 전송
        String eventId = createIdWithMemberId(memberId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + memberId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        // Last-Event-ID 값이 헤더에 있는 경우, 저장된 데이터 캐시에서 id값과 Last-Event-ID 값을 통해 유실된 데이터들만 다시 보내준다.
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, memberId, emitterId, emitter);
        }

        return new NotificationSubscribeResponseDto(emitter);
    }

    /**
     * 클라이언트에게 데이터를 전송한다.
     *
     * @param emitter 이벤트 emitter
     * @param emitterId 이벤트 emitter의 아이디
     * @param data 전송할 데이터
     */
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) throws BaseException {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepositoryImpl.deleteById(emitterId);
            throw new BaseException(BaseResponseStatus.NOTIFICATION_SEND_FAILED);
        }
    }

    /**
     * 사용자 아이디를 기반으로 EmitterID/EventID를 생성한다.
     *
     * @param memberId 데이터를 받을 사용자의 아이디
     * @return Id 사용자 아이디 + 현재 시간으로 구성된 이미터/이벤트 아이디
     */
    private String createIdWithMemberId(UUID memberId) {
        // Last-Event-ID를 구분하기 위해 현재 시간을 포함시켜 데이터가 유실된 시점을 표시
        return memberId + "_" + System.currentTimeMillis();
    }

    /**
     * 클라이언트가 미수신한 Event 목록이 존재하는지 확인한다.
     *
     * @param lastEventId 클라이언트가 마지막으로 수신한 이벤트의 아이디
     * @return boolean 클라이언트가 미수신한 Event 목록의 존재 여부
     */
    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    /**
     * 클라이언트가 미수신한 Event 목록을 전송한다.
     *
     * @param lastEventId 클라이언트가 마지막으로 수신한 이벤트의 아이디
     * @param memberId 클라이언트의 아이디
     * @param emitterId 클라이언트가 구독한 Emitter 아이디
     * @param emitter 클라이언트가 구독한 Emitter
     */
    private void sendLostData(String lastEventId, UUID memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepositoryImpl.findAllEventCacheStartWithByMemberId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
        /*
        전송 실패 예외처리
        */
    }
    
    /*
    데이터 전송
     */
    /**
     * 서버의 이벤트를 클라이언트에게 보낸다.
     * 클라이언트가 여러 브라우저에서 접속할 수 있기 때문에 emitter가 여러개 일 수 있다.
     * 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
     *
     * @param receiver 알림을 수신할 사용자 정보
     * @param notificationType 알림 유형
     * @param content 알림 내용
     * @param url 연결 url
     */
//    @Transactional
    public void send(Member receiver, NotificationType notificationType, String content, String url) {
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));

        String receiverId = String.valueOf(receiver.getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepositoryImpl.findAllEmitterStartWithByMemberId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepositoryImpl.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, new NotificationResponseDto(notification));
                    /*
        전송 실패 예외처리
        */
                }
        );
    }

//    public void sendList(List receiverList, String content, String type, String urlValue) {
//        List<Notification> notifications = new ArrayList<>();
//        Map<String, SseEmitter> sseEmitters;
//
//        for (int i = 0; i < receiverList.size(); i++) {
//            int finalI = i;
//            sseEmitters = new HashMap<>();
//            notifications.add(createNotification(receiverList.get(i).toString(), content, type, urlValue));
//            sseEmitters.putAll(emitterRepository.findAllEmitterStartWithByEmail(receiverList.get(i).toString()));
//            sseEmitters.forEach(
//                    (key, emitter) -> {
//                        // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
//                        emitterRepository.saveEventCache(key, notifications.get(finalI));
//                        // 데이터 전송
//                        sendToClient(emitter, key, notifications.get(finalI));
//                    }
//            );
//        }
//    }

    /**
     * 클라이언트에게 보낼 알림 객체를 생성한다.
     *
     * @param receiver 알림을 수신할 사용자 정보
     * @param notificationType 알림 유형
     * @param content 알림 내용
     * @param url 연결 url
     */
    private Notification createNotification(Member receiver, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }
}
