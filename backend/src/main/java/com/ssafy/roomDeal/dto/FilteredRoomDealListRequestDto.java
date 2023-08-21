package com.ssafy.roomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilteredRoomDealListRequestDto {

    private String address;

    private List<String> roomType;

    private List<String> oneroomType;

    private Integer startFloor;

    private Integer endFloor;

    private Integer startMonthlyFee;

    private Integer endMonthlyFee;

    private Integer startDeposit;

    private Integer endDeposit;

    private Integer startManagementFee;

    private Integer endManagementFee;

    private Double startRoomSize;

    private Double endRoomSize;

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
}
