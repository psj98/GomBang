package com.ssafy.roomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class RoomDealDeleteResponseDto {

    @NotNull
    private Long id;
}
