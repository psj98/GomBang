package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchByAddressRequestDto implements Serializable {

    private String address; // 지번 주소

    private String content; // 본문 내용
}
