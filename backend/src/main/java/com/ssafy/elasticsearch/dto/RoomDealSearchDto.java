package com.ssafy.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Document(indexName = "rooms_data")
@Mapping(mappingPath = "json/roomDealMappings.json")
@Setting(settingPath = "json/roomDealSettings.json")
public class RoomDealSearchDto implements Serializable {

    @Id
    private String id;
    private Long roomId;
    private String address;
    private SearchByStationUnivRequestDto location;
    private String content;

    @Builder
    public RoomDealSearchDto(String id, Long roomId, String address, SearchByStationUnivRequestDto location, String content) {
        this.id = id;
        this.roomId = roomId;
        this.address = address;
        this.location = location;
        this.content = content;
    }
}
