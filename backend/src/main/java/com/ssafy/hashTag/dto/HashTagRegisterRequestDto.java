package com.ssafy.hashTag.dto;

import lombok.Getter;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Getter
public class HashTagRegisterRequestDto {

    private List<String> hashTagNames;
}
