package com.ssafy.elasticsearch.dto;

import lombok.Data;

/**
 * 주소로 매물 검색
 */
@Data
public class SearchByAddressRequestDto {

    private String address; // 지번 주소
    private String content; // 본문 내용
}
