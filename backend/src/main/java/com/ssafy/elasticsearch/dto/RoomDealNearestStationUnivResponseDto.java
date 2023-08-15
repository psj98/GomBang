package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 가까운 역, 대학교 결과
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDealNearestStationUnivResponseDto implements Serializable {

    private String stationName; // 가까운 역 이름
    private String univName; // 가까운 대학교 이름
}
