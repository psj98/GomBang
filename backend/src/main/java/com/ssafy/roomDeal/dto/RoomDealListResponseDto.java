package com.ssafy.roomDeal.dto;

import com.ssafy.redis.RoomDealRedisStoreDto;
import com.ssafy.roomDeal.domain.RoomDeal;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class RoomDealListResponseDto {

    private Long roomDealId;

    private String jibunAddress;

    private String roomType;

    private String oneroomType;

    private Integer monthlyFee;

    private Integer deposit;

    private Integer managementFee;

    private Double roomSize;

    private Integer floor;

    public RoomDealListResponseDto(RoomDeal roomDeal) {
        this.roomDealId = roomDeal.getId();
        this.jibunAddress = roomDeal.getJibunAddress();
        this.roomType = roomDeal.getRoomType();
        this.oneroomType = roomDeal.getOneroomType();
        this.monthlyFee = roomDeal.getMonthlyFee();
        this.deposit = roomDeal.getDeposit();
        this.managementFee = roomDeal.getManagementFee();
        this.roomSize = roomDeal.getRoomSize();
        this.floor = roomDeal.getFloor();
    }

    public RoomDealListResponseDto(RoomDealRedisStoreDto roomDealRedisStoreDto) {
        this.roomDealId = roomDealRedisStoreDto.getId();
        this.jibunAddress = roomDealRedisStoreDto.getJibunAddress();
        this.roomType = roomDealRedisStoreDto.getRoomType();
        this.oneroomType = roomDealRedisStoreDto.getOneroomType();
        this.monthlyFee = roomDealRedisStoreDto.getMonthlyFee();
        this.deposit = roomDealRedisStoreDto.getDeposit();
        this.managementFee = roomDealRedisStoreDto.getManagementFee();
        this.roomSize = roomDealRedisStoreDto.getRoomSize();
        this.floor = roomDealRedisStoreDto.getFloor();
    }
}
