package com.ssafy.roomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDealDeleteRequestDto {

    @NotNull
    private Long roomDealId;

    @NotNull
    private UUID memberId;
}
