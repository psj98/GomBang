package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 검색어 매핑
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRelatedListRequestDto {

    private String searchWord; // 검색어 (주소, 역, 학교 포함)
}
