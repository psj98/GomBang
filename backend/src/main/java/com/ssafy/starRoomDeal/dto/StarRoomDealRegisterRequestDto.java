package com.ssafy.starRoomDeal.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class StarRoomDealRegisterRequestDto {

    @NotNull
    private UUID memberId;

    @NotNull
    private Long roomDealId;
}
