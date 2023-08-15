package com.ssafy.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * showRoom 검색 (검색어 + 해시태그) 리스트
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowRoomSearchRequestDto implements Serializable {

    private UUID memberId; // 로그인 한 유저 id
    private String searchWord; // 검색어
    private String searchType; // 검색어 유형 (주소, 역, 대학교)
    private String hashTag; // 해시 태그
    private String sortType; // 정렬 타입 (내림차순 desc, 오름차순 asc)
    private int pageOffset; // 페이징 시작
}
