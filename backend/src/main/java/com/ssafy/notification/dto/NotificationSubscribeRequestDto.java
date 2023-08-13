package com.ssafy.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSubscribeRequestDto {

    @NotNull
    private UUID memberId;
}
