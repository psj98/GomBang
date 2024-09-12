package com.ssafy.starRoomDeal.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class StarRoomDealMyListRequestDto {

    @NotNull
    private UUID memberId;
}
