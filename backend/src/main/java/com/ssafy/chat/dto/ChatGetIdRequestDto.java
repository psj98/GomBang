package com.ssafy.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ChatGetIdRequestDto {
    @NotNull
    private UUID grantorId;

    @NotNull
    private UUID assigneeId;
}