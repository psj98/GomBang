package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 위도, 경도
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDto implements Serializable {

    private String lat;
    private String lon;
}
