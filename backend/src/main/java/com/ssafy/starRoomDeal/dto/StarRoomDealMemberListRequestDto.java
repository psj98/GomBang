package com.ssafy.starRoomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StarRoomDealMemberListRequestDto {

    @NotNull
    private Long roomDealId;
}
