package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 역, 학교로 매물 검색
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchByStationUnivRequestDto implements Serializable {

    private String lat; // 위도
    private String lon; // 경도
    private String content; // 본문 내용

    public SearchByStationUnivRequestDto(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
