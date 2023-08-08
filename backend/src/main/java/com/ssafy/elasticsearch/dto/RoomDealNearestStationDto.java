package com.ssafy.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@Document(indexName = "station_info")
@Mapping(mappingPath = "json/stationMappings.json")
public class RoomDealNearestStationDto {

    @Id
    private String id;
    private String name;
    private String address;
    private SearchNearestStationUnivRequestDto location;

    @Builder
    public RoomDealNearestStationDto(String id, String name, String address, SearchNearestStationUnivRequestDto location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
    }
}
