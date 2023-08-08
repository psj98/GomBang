package com.ssafy.roomDeal.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class RoomDealRegisterOptionDto {

    @NotNull
    private boolean airConditioner;

    @NotNull
    private boolean refrigerator;

    @NotNull
    private boolean washer;

    @NotNull
    private boolean dryer;

    @NotNull
    private boolean sink;

    @NotNull
    private boolean gasRange;

    @NotNull
    private boolean closet;

    @NotNull
    private boolean shoeCloset;

    @NotNull
    private boolean fireAlarm;

    @NotNull
    private boolean elevator;

    @NotNull
    private boolean parkingLot;
}
