package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * ShowRoom 저장
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "showroom_data")
@Mapping(mappingPath = "json/showRoomMappings.json")
@Setting(settingPath = "json/showRoomSettings.json")
public class ShowRoomSaveDto {

    @Id
    private String id; // 구분 id
    private Integer showRoomId; // 곰방봐 id
    private String address; // 지번 주소
    private String station; // 역
    private String univ; // 대학교
    private String hashTag; // 해시 태그
    private String registerTime; // 등록 시간
}
