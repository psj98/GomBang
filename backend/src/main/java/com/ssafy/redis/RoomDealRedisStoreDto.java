package com.ssafy.redis;

import com.ssafy.roomDeal.domain.RoomDealOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDealRedisStoreDto {

    private Long id;

    private String roomType;

    private Double roomSize;

    private String oneroomType;

    private Integer bathroomCount;

    private String roadAddress;

    private String jibunAddress;

    private Integer monthlyFee;

    private Integer deposit;

    private Integer managementFee;

    private Integer floor;

    private boolean airConditioner;

    private boolean refrigerator;

    private boolean washer;

    private boolean dryer;

    private boolean sink;

    private boolean gasRange;

    private boolean closet;

    private boolean shoeCloset;

    private boolean fireAlarm;

    private boolean elevator;

    private boolean parkingLot;

    public RoomDealRedisStoreDto(RoomDealOption roomDealOption) {
        this.id = roomDealOption.getId();
        this.roomSize = roomDealOption.getRoomdeal().getRoomSize();
        this.roomType = roomDealOption.getRoomdeal().getRoomType();
        this.oneroomType = roomDealOption.getRoomdeal().getOneroomType();
        this.bathroomCount = roomDealOption.getRoomdeal().getBathroomCount();
        this.roadAddress = roomDealOption.getRoomdeal().getRoadAddress();
        this.jibunAddress = roomDealOption.getRoomdeal().getJibunAddress();
        this.monthlyFee = roomDealOption.getRoomdeal().getMonthlyFee();
        this.managementFee = roomDealOption.getRoomdeal().getManagementFee();
        this.deposit = roomDealOption.getRoomdeal().getDeposit();
        this.floor = roomDealOption.getRoomdeal().getFloor();
        this.refrigerator = roomDealOption.isRefrigerator();
        this.washer = roomDealOption.isWasher();
        this.dryer = roomDealOption.isDryer();
        this.sink = roomDealOption.isSink();
        this.gasRange = roomDealOption.isGasRange();
        this.closet = roomDealOption.isCloset();
        this.shoeCloset = roomDealOption.isShoeCloset();
        this.fireAlarm = roomDealOption.isFireAlarm();
        this.elevator = roomDealOption.isElevator();
        this.parkingLot = roomDealOption.isParkingLot();

    }

}
