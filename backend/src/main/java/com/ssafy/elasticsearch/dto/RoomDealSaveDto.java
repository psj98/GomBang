package com.ssafy.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * 매물 저장
 */
@Data
@NoArgsConstructor
@Document(indexName = "rooms_data")
@Mapping(mappingPath = "json/roomDealMappings.json")
@Setting(settingPath = "json/roomDealSettings.json")
public class RoomDealSaveDto {

    @Id
    private String id; // 구분 id
    private Long roomId; // 방 번호
    private String address; // 지번 주소
    private SearchByStationUnivRequestDto location; // 위도, 경도
    private String content; // 본문 내용

    @Builder
    public RoomDealSaveDto(String id, Long roomId, String address, SearchByStationUnivRequestDto location, String content) {
        this.id = id;
        this.roomId = roomId;
        this.address = address;
        this.location = location;
        this.content = content;
    }
}
