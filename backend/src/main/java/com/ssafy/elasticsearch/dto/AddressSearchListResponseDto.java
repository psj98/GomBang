package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * 검색어 매핑 결과
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "address_mapping")
@Mapping(mappingPath = "json/addressListMappings.json")
@Setting(settingPath = "json/addressListSettings.json")
public class AddressSearchListResponseDto {

    private String address; // 지번 주소
}
