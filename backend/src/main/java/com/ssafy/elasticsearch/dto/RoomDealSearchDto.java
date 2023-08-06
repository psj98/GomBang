package com.ssafy.elasticsearch.dto;

import com.ssafy.roomDeal.dto.SearchNearestStationUnivRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@NoArgsConstructor
@Document(indexName = "rooms_data")
@Mapping(mappingPath = "json/roomDealMappings.json")
@Setting(settingPath = "json/roomDealSettings.json")
public class RoomDealSearchDto {

    @Id
    private String id;
    private Long roomId;
    private String address;
    private SearchNearestStationUnivRequestDto location;
    private String content;

    @Builder
    public RoomDealSearchDto(String id, Long roomId, String address, SearchNearestStationUnivRequestDto location, String content) {
        this.id = id;
        this.roomId = roomId;
        this.address = address;
        this.location = location;
        this.content = content;
    }

    @Override
    public String toString() {
        return "RoomDealSearchDto{" +
                "id='" + id + '\'' +
                ", roomId=" + roomId +
                ", address='" + address + '\'' +
                ", location=" + location +
                ", content='" + content + '\'' +
                '}';
    }
}
