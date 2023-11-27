package com.ssafy.showRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ShowRoomDeleteResponseDto {

    @NotNull
    private Integer showRoomId;
}
