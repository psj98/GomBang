package com.ssafy.roomDeal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchNearestStationUnivRequestDto {

    private String lat;

    private String lon;

}
