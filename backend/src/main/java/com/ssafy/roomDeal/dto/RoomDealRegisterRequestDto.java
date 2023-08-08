package com.ssafy.roomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDealRegisterRequestDto {

    @NotNull
    private RoomDealRegisterDefaultDto roomDealRegisterDefaultDto;

    @NotNull
    private RoomDealRegisterOptionDto roomDealRegisterOptionDto;
}
