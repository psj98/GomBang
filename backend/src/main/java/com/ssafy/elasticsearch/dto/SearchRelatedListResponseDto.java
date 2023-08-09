package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * 검색어 매핑 결과
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "search_related_list")
@Mapping(mappingPath = "json/searchRelatedListMappings.json")
@Setting(settingPath = "json/searchRelatedListSettings.json")
public class SearchRelatedListResponseDto {

    private String address; // 지번 주소
    private String station; // 역 이름
    private String univ; // 대학교 이름
    private String searchType; // 검색어 종류 (주소, 역, 대학교)
}
