package com.ssafy.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import javax.persistence.Id;

/**
 * 매물 주소에 따른 가까운 역 조회
 */
@Data
@NoArgsConstructor
@Document(indexName = "nearest_station_info")
@Mapping(mappingPath = "json/nearestStationMappings.json")
public class RoomDealNearestStationResponseDto {

    private String name; // 역 이름

    @Builder
    public RoomDealNearestStationResponseDto(String name) {
        this.name = name;
    }
}
