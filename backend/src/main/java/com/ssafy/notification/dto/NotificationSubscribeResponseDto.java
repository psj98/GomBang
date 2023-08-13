package com.ssafy.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSubscribeResponseDto {

    private SseEmitter emitter;
}
