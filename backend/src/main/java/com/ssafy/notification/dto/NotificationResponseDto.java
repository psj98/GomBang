package com.ssafy.notification.dto;

import com.ssafy.notification.domain.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class NotificationResponseDto {

    private Long id;

    private String content;

    private String url;

    private Boolean isRead;

    @Builder
    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.url = notification.getUrl();
        this.isRead = notification.getIsRead();
    }
}
