package com.ssafy.live.domain;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@EqualsAndHashCode
public class RtcRoom {
    @NotNull
    private String roomId;
    private Map<String, WebSocketSession> userList;
}
