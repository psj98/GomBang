package com.ssafy.elasticsearch.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 주소로 매물 검색
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchByAddressRequestDto implements Serializable {

    private String address; // 지번 주소
    private String content; // 본문 내용
}
