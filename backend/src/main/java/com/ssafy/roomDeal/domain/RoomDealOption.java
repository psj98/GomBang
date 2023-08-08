package com.ssafy.roomDeal.domain;

import com.ssafy.roomDeal.dto.RoomDealRegisterOptionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDealOption {

    @Id
    @Column(name = "room_deal_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "room_deal_id")
    private RoomDeal roomdeal;

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

    public RoomDealOption(RoomDeal newRoomDeal, RoomDealRegisterOptionDto roomDealRegisterOptionDto) {
        this.roomdeal = newRoomDeal;
        this.airConditioner = roomDealRegisterOptionDto.isAirConditioner();
        this.refrigerator = roomDealRegisterOptionDto.isRefrigerator();
        this.washer = roomDealRegisterOptionDto.isWasher();
        this.dryer = roomDealRegisterOptionDto.isDryer();
        this.sink = roomDealRegisterOptionDto.isSink();
        this.gasRange = roomDealRegisterOptionDto.isGasRange();
        this.closet = roomDealRegisterOptionDto.isCloset();
        this.shoeCloset = roomDealRegisterOptionDto.isShoeCloset();
        this.fireAlarm = roomDealRegisterOptionDto.isFireAlarm();
        this.elevator = roomDealRegisterOptionDto.isElevator();
        this.parkingLot = roomDealRegisterOptionDto.isParkingLot();
    }
}