package com.ssafy.roomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class RoomDealUpdateRequestDto {

    @NotNull
    private Long id;

    @NotNull
    private Integer monthlyFee;

    @NotNull
    private Integer deposit;

    @NotNull
    private Integer managementFee;
}
