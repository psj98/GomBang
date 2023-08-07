package com.ssafy.elasticsearch.dto;

import lombok.Getter;

@Getter
public class SearchByAddressRequestDto {

    private String address; // 지번 주소

    private String content; // 본문 내용
}
