package com.ssafy.roomDeal.dto;

import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.domain.RoomDealOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class RoomDealResponseDto implements Serializable {

    @NotNull
    private RoomDeal roomDeal;

    @NotNull
    private RoomDealOption roomDealOption;
}
