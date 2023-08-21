package com.ssafy.starRoomDeal.dto;

import com.ssafy.starRoomDeal.domain.StarRoomDealMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StarRoomDealMyListResponseDto {

    private List<StarRoomDealMapping> starRoomDealList;
}
