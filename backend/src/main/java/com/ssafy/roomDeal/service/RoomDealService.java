package com.ssafy.roomDeal.service;

import com.ssafy.elasticsearch.dto.RoomDealSearchDto;
import com.ssafy.roomDeal.dto.SearchByAddressRequestDto;
import com.ssafy.roomDeal.dto.SearchNearestStationUnivRequestDto;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomDealService {

    private final ElasticsearchOperations elasticsearchOperations;

    // 지번주소로 매물 검색
    public List<RoomDealSearchDto> searchByAddress(SearchByAddressRequestDto searchByAddressRequestDto) {
        // match_phrase query 생성
        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("address", searchByAddressRequestDto.getAddress());
        System.out.println(matchPhraseQuery); // query 확인

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(matchPhraseQuery); // match_phrase query를 _search 안에 저장

        // 결과 출력
        SearchHits<RoomDealSearchDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchDto.class, IndexCoordinates.of("rooms_data"));

        System.out.println(articles); // _search 결과 확인

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealSearchDto>> searchHitList = articles.getSearchHits();
        ArrayList<RoomDealSearchDto> list = new ArrayList<>();
        for (SearchHit<RoomDealSearchDto> item : searchHitList) {
            list.add(item.getContent());
        }

        return list;
    }

    // 위도, 경도로 매물 찾기
    public List<RoomDealSearchDto> searchByLocation(SearchNearestStationUnivRequestDto searchNearestStationUnivRequestDto) {
        double lat = Double.parseDouble(searchNearestStationUnivRequestDto.getLat());
        double lon = Double.parseDouble(searchNearestStationUnivRequestDto.getLon());

        // geo_point query 생성
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
//                .point(37.541609091148, 127.0717799526) // lat, lng
                .point(lat, lon)
                .distance(1000, DistanceUnit.KILOMETERS);
        System.out.println(geoDistanceQueryBuilder); // query 확인

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // match_phrase query를 _search 안에 저장
        queryBuilder.withQuery(geoDistanceQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .geoDistanceSort("location", lat, lon)
                        .order(SortOrder.ASC)
                        .sortMode(SortMode.MIN))
                .withPageable(PageRequest.of(0, 100)).build(); // size 제한 (100개)

        System.out.println(queryBuilder);

        // 결과 출력
        SearchHits<RoomDealSearchDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchDto.class, IndexCoordinates.of("rooms_data"));
        System.out.println(articles); // _search 결과 확인

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealSearchDto>> searchHitList = articles.getSearchHits();
        ArrayList<RoomDealSearchDto> list = new ArrayList<>();
        for (SearchHit<RoomDealSearchDto> item : searchHitList) {
            list.add(item.getContent());
        }

        return list;
    }
}
