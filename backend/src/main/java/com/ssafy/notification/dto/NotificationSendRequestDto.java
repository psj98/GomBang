package com.ssafy.notification.dto;

import com.ssafy.member.domain.Member;
import com.ssafy.notification.domain.NotificationContent;
import com.ssafy.notification.domain.NotificationType;
import com.ssafy.notification.domain.RelatedUrl;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSendRequestDto {

    @NotNull
    private Member receiver;

    @NotNull
    private NotificationType notificationType;

    @NotNull
    private NotificationContent notificationContent;

    @NotNull
    private RelatedUrl relatedUrl;

//    public String getContent() {
//        return content.getContent();
//    }
//
//    public String getUrl() {
//        return url.getUrl();
//    }

}
