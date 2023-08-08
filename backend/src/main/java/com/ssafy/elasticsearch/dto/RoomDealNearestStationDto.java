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
@Document(indexName = "station_info")
@Mapping(mappingPath = "json/stationMappings.json")
public class RoomDealNearestStationDto {

    @Id
    private String id; // 구분 id
    private String name; // 역 이름
    private String address; // 역 지번 주소
    private SearchByStationUnivRequestDto location; // 위도, 경도

    @Builder
    public RoomDealNearestStationDto(String id, String name, String address, SearchByStationUnivRequestDto location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
    }
}
