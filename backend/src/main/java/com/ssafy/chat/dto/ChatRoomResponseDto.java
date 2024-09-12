package com.ssafy.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatRoomResponseDto {

    @NotNull
    private UUID roomId;
}
