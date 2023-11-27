package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;

/**
 * showRoom 검색 결과
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "showroom_data")
@Mapping(mappingPath = "json/showRoomMappings.json")
@Setting(settingPath = "json/showRoomSettings.json")
public class ShowRoomSearchResponseDto implements Serializable {

    private Integer showRoomId; // 곰방봐 id
}
