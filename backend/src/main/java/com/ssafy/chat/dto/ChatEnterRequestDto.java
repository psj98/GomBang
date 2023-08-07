package com.ssafy.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatEnterRequestDto {

    @NotNull
    private UUID roomId;

    @NotNull
    private UUID userId;
}
