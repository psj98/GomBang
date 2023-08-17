package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 검색어 매핑 최종 결과
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRelatedListUniteResponseDto {

    private String searchWord; // 검색어
    private String searchType; // 검색어 타입
    private String lat; // 위도
    private String lon; // 경도
}
