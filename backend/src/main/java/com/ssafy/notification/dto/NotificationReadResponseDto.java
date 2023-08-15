package com.ssafy.notification.dto;

import com.ssafy.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReadResponseDto {

    @NotNull
    private Notification notification;

}
