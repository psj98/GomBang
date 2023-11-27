package com.ssafy.chat.dto;

import com.ssafy.chat.domain.Chat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class ChatHistoryResponseDto {

    @NotNull
    private List<Chat> history;
}
