package com.ssafy.roomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class RoomDealUpdateRequestDto {

    @NotNull
    private Long roomDealId;

    @NotNull
    private UUID memberId;

    @NotNull
    private Integer monthlyFee;

    @NotNull
    private Integer deposit;

    @NotNull
    private Integer managementFee;
}
