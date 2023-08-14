package com.ssafy.notification.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationContent {

    @NotNull
    private String content;

    public NotificationContent(String content){
        this.content = content;
    }
}
