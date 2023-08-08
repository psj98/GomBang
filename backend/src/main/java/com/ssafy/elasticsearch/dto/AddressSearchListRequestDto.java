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
public class AddressSearchListRequestDto {

    private String address; // 지번 주소
}
