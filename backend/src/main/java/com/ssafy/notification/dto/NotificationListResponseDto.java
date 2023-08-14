package com.ssafy.notification.dto;

import com.ssafy.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResponseDto {

    @NotNull
    private List<Notification> notificationList;

}
