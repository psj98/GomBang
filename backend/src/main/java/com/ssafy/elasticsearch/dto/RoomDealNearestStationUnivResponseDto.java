package com.ssafy.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 가까운 역, 대학교 결과
 */
@Data
@NoArgsConstructor
public class RoomDealNearestStationUnivResponseDto {

    private String stationName; // 가까운 역 이름
    private String univName; // 가까운 대학교 이름

    @Builder
    public RoomDealNearestStationUnivResponseDto(String stationName, String univName) {
        this.stationName = stationName;
        this.univName = univName;
    }
}
