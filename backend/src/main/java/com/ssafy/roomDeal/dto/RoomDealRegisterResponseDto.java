package com.ssafy.roomDeal.dto;

import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.domain.RoomDealOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class RoomDealRegisterResponseDto {

    @NotNull
    private RoomDeal roomDeal;

    @NotNull
    private RoomDealOption roomDealOption;
}
