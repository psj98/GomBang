package com.ssafy.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Document(indexName = "stations")
@Mapping(mappingPath = "json/stationMappings.json")
public class RoomDealNearestStationDto implements Serializable {

    @Id
    private String id;
    private String stationName;
    private SearchByStationUnivRequestDto location;

    @Builder
    public RoomDealNearestStationDto(String id, String stationName, SearchByStationUnivRequestDto location) {
        this.id = id;
        this.stationName = stationName;
        this.location = location;
    }
}
