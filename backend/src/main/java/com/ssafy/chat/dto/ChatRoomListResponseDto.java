package com.ssafy.chat.dto;

import com.ssafy.chat.domain.ChatRoom;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatRoomListResponseDto {

    @NotNull
    private List<ChatRoom> list;
}
