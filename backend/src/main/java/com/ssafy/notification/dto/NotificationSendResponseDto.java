package com.ssafy.notification.dto;

import com.ssafy.notification.domain.Notification;
import com.ssafy.notification.domain.NotificationType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class NotificationSendResponseDto {

    @NotNull
    private Long id;

    @NotNull
    private NotificationType notificationType;

    @NotNull
    private String notificationContent;

    @NotNull
    private String relatedUrl;

    @Builder
    public NotificationSendResponseDto(Notification notification) {
        this.id = notification.getId();
        this.notificationType = notification.getNotificationType();
        this.notificationContent = notification.getContent();
        this.relatedUrl = notification.getUrl();
    }
}
