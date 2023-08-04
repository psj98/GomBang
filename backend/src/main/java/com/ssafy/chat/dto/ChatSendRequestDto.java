package com.ssafy.chat.dto;

import com.ssafy.chat.domain.ChatDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ChatSendRequestDto {

    @NotNull
    private ChatDTO chat;
}
