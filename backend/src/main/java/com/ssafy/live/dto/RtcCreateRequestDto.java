package com.ssafy.live.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class RtcCreateRequestDto {
    @NotNull
    String roomId;
}
