package com.ssafy.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * 매물 주소에 따른 가까운 역 조회
 */
@Data
@NoArgsConstructor
@Document(indexName = "nearest_univ_info")
@Mapping(mappingPath = "json/nearestUnivMappings.json")
public class RoomDealNearestUnivResponseDto {

    private String name; // 역 이름

    @Builder
    public RoomDealNearestUnivResponseDto(String name) {
        this.name = name;
    }
}
