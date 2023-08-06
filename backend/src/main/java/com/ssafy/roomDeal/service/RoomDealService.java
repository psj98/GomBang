package com.ssafy.roomDeal.service;

import com.ssafy.elasticsearch.dto.RoomDealSearchDto;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.domain.RoomDealOption;
import com.ssafy.roomDeal.dto.*;
import com.ssafy.roomDeal.repository.RoomDealElasticSearchRepository;
import com.ssafy.roomDeal.repository.RoomDealOptionReposiroty;
import com.ssafy.roomDeal.repository.RoomDealRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomDealService {

    private final ElasticsearchOperations elasticsearchOperations;

    private final RoomDealRepository roomDealRepository;
    private final RoomDealOptionReposiroty roomDealOptionReposiroty;
    private final RoomDealElasticSearchRepository roomDealElasticSearchRepository;

    // 지번주소로 매물 검색
    public List<RoomDealSearchDto> searchByAddress(SearchByAddressRequestDto searchByAddressRequestDto) {
        // match_phrase query 생성
        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("address", searchByAddressRequestDto.getAddress());

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(matchPhraseQuery); // match_phrase query를 _search 안에 저장

        if(!searchByAddressRequestDto.getContent().isEmpty()){
            // match_phrase query 생성
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("content.nori", searchByAddressRequestDto.getContent());
            queryBuilder.withQuery(matchQuery);
        }

        // 결과 출력
        SearchHits<RoomDealSearchDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchDto.class, IndexCoordinates.of("rooms_data"));

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealSearchDto>> searchHitList = articles.getSearchHits();
        ArrayList<RoomDealSearchDto> roomDealSearchDtoList = new ArrayList<>();
        for (SearchHit<RoomDealSearchDto> item : searchHitList) {
            roomDealSearchDtoList.add(item.getContent());
        }

        return roomDealSearchDtoList;
    }

    // 위도, 경도로 매물 찾기
    public List<RoomDealSearchDto> searchByLocation(SearchNearestStationUnivRequestDto searchNearestStationUnivRequestDto) {
        double lat = Double.parseDouble(searchNearestStationUnivRequestDto.getLat());
        double lon = Double.parseDouble(searchNearestStationUnivRequestDto.getLon());

        // geo_point query 생성
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(lat, lon)
                .distance(1000, DistanceUnit.KILOMETERS);

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // match_phrase query를 _search 안에 저장
        queryBuilder.withQuery(geoDistanceQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .geoDistanceSort("location", lat, lon) // 거리 기준 오름차순 정렬
                        .order(SortOrder.ASC)
                        .sortMode(SortMode.MIN))
                .withPageable(PageRequest.of(0, 100)).build(); // size 제한 (100개)

        if(!searchNearestStationUnivRequestDto.getContent().isEmpty()){
            // match_phrase query 생성
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("content.nori", searchNearestStationUnivRequestDto.getContent());
            queryBuilder.withQuery(matchQuery);
        }

        // 결과 출력
        SearchHits<RoomDealSearchDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchDto.class, IndexCoordinates.of("rooms_data"));

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealSearchDto>> searchHitList = articles.getSearchHits();
        ArrayList<RoomDealSearchDto> roomDealSearchDtoList = new ArrayList<>();
        for (SearchHit<RoomDealSearchDto> item : searchHitList) {
            roomDealSearchDtoList.add(item.getContent());
        }

        return roomDealSearchDtoList;
    }

    // 매물 등록
    @Transactional
    public RoomDealResponseDto registerRoomDeal(RoomDealRegisterRequestDto roomDealRegisterRequestDto) {
        RoomDeal newRoomDeal = new RoomDeal(roomDealRegisterRequestDto.getRoomDealRegisterDefaultDto());
        RoomDealOption newRoomDealOption = new RoomDealOption(newRoomDeal, roomDealRegisterRequestDto.getRoomDealRegisterOptionDto());

        roomDealRepository.save(newRoomDeal);
        roomDealOptionReposiroty.save(newRoomDealOption);

        /* ES 매물 등록 - 추후 Position 수정 */
        try {
            roomDealElasticSearchRepository.save(new RoomDealSearchDto(String.valueOf(newRoomDeal.getId()), newRoomDeal.getId(), newRoomDeal.getJibunAddress(), new SearchNearestStationUnivRequestDto("37.1", "121.1"), newRoomDeal.getContent()));
        } catch (Exception e) {
            return new RoomDealResponseDto(newRoomDeal, newRoomDealOption);
        }

        return new RoomDealResponseDto(newRoomDeal, newRoomDealOption);
    }

    // 매믈 조회
    public RoomDealResponseDto getRoomDeal(Long id) {
        Optional<RoomDeal> roomDeal = roomDealRepository.findById(id);
        Optional<RoomDealOption> roomDealOption = roomDealOptionReposiroty.findById(id);

        if (roomDeal.isPresent() && roomDealOption.isPresent()) {
            return new RoomDealResponseDto(roomDeal.get(), roomDealOption.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 roomDeal입니다.");
        }
    }

    // 매물 수정
    @Transactional
    public RoomDealResponseDto updateRoomDeal(RoomDealUpdateRequestDto roomDealUpdateRequestDto) {
        RoomDeal roomdeal = roomDealRepository.findById(roomDealUpdateRequestDto.getId()).get();

        roomdeal.roomDealUpdate(roomDealUpdateRequestDto);

        RoomDealOption roomDealOption = roomDealOptionReposiroty.findById(roomDealUpdateRequestDto.getId()).get();

        return new RoomDealResponseDto(roomdeal, roomDealOption);

    }

    // 본문 검색
    public List<RoomDealSearchDto> searchByContent(String sentence) {
        // match_phrase query 생성
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("content.nori", sentence);

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(matchQuery); // match_phrase query를 _search 안에 저장

        // 결과 출력
        SearchHits<RoomDealSearchDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchDto.class, IndexCoordinates.of("rooms_data"));

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealSearchDto>> searchHitList = articles.getSearchHits();
        ArrayList<RoomDealSearchDto> roomDealSearchDtoList = new ArrayList<>();
        for (SearchHit<RoomDealSearchDto> item : searchHitList) {
            roomDealSearchDtoList.add(item.getContent());
        }

        return roomDealSearchDtoList;
    }

    // 매물 삭제
    @Transactional
    public RoomDealDeleteResponseDto deleteRoomDeal(Long id) {
        roomDealOptionReposiroty.deleteById(id);
        roomDealRepository.deleteById(id);

        return new RoomDealDeleteResponseDto(id);
    }
}
