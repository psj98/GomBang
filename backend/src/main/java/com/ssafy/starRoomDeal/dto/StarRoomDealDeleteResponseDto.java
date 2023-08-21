package com.ssafy.starRoomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class StarRoomDealDeleteResponseDto {

    @NotNull
    private UUID memberId;

    @NotNull
    private Long roomDealId;
}
