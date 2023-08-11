package com.ssafy.roomDeal.service;

import com.ssafy.elasticsearch.dto.*;
import com.ssafy.elasticsearch.repository.RoomDealElasticSearchRepository;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.member.domain.Member;
import com.ssafy.member.repository.MemberRepository;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.domain.RoomDealOption;
import com.ssafy.roomDeal.dto.*;
import com.ssafy.roomDeal.repository.RoomDealOptionReposiroty;
import com.ssafy.roomDeal.repository.RoomDealRedisRepository;
import com.ssafy.roomDeal.repository.RoomDealRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomDealService {

    private final ElasticsearchOperations elasticsearchOperations;

    private final MemberRepository memberRepository;
    private final RoomDealRepository roomDealRepository;
    private final RoomDealOptionReposiroty roomDealOptionReposiroty;
    private final RoomDealRedisRepository roomDealRedisRepository;
    private final RoomDealElasticSearchRepository roomDealElasticSearchRepository;

    // 매물 등록
    @Transactional
    public RoomDealResponseDto registerRoomDeal(RoomDealRegisterRequestDto roomDealRegisterRequestDto) {
        Member newMember = memberRepository.findById(roomDealRegisterRequestDto.getRoomDealRegisterDefaultDto().getId()).get();
        RoomDeal newRoomDeal = new RoomDeal(roomDealRegisterRequestDto.getRoomDealRegisterDefaultDto(), newMember);
        RoomDealOption newRoomDealOption = new RoomDealOption(newRoomDeal, roomDealRegisterRequestDto.getRoomDealRegisterOptionDto());

        roomDealRepository.save(newRoomDeal);
        roomDealOptionReposiroty.save(newRoomDealOption);

        String id = String.valueOf(newRoomDeal.getId());
        Long roomId = newRoomDeal.getId();
        String address = newRoomDeal.getJibunAddress();
        SearchByStationUnivRequestDto searchByStationUnivRequestDto = new SearchByStationUnivRequestDto("37.1", "127.1");
        String content = newRoomDeal.getContent();

        RoomDealSearchResponseDto roomDealSearchResponseDto = new RoomDealSearchResponseDto(id, roomId, address, searchByStationUnivRequestDto, content);

        /* ES 매물 등록 - 추후 Position 수정 */
        try {
            roomDealElasticSearchRepository.save(roomDealSearchResponseDto);
        } catch (Exception e) {
            return new RoomDealResponseDto(newRoomDeal, newRoomDealOption);
        }

        return new RoomDealResponseDto(newRoomDeal, newRoomDealOption);
    }

    // 매믈 조회
    public RoomDealResponseDto getRoomDeal(Long id) throws BaseException {
        Optional<RoomDeal> roomDeal = roomDealRepository.findById(id);
        Optional<RoomDealOption> roomDealOption = roomDealOptionReposiroty.findById(id);

        if (roomDeal.isPresent() && roomDealOption.isPresent()) {
            return new RoomDealResponseDto(roomDeal.get(), roomDealOption.get());
        } else {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
        }
    }

    // 매물 수정
    @Transactional
    public RoomDealResponseDto updateRoomDeal(RoomDealUpdateRequestDto roomDealUpdateRequestDto) throws BaseException {
        Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealUpdateRequestDto.getRoomDealId());
        Optional<RoomDealOption> roomDealOptionOptional = roomDealOptionReposiroty.findById(roomDealUpdateRequestDto.getRoomDealId());

        if (roomDealOptional.isPresent() && roomDealOptionOptional.isPresent()) {
            RoomDeal roomDeal = roomDealOptional.get();

            // 본인 확인
            if (roomDeal.getMember().getId().equals(roomDealUpdateRequestDto.getMemberId())) {
                roomDeal.roomDealUpdate(roomDealUpdateRequestDto);
                return new RoomDealResponseDto(roomDealOptional.get(), roomDealOptionOptional.get());
            } else {
                throw new BaseException(BaseResponseStatus.NOT_AUTHORIZED);
            }
        } else {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
        }
    }

    // 매물 삭제
    @Transactional
    public RoomDealDeleteResponseDto deleteRoomDeal(RoomDealDeleteRequestDto roomDealDeleteRequestDto) throws BaseException {

        Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealDeleteRequestDto.getRoomDealId());
        Optional<RoomDealOption> roomDealOptionOptional = roomDealOptionReposiroty.findById(roomDealDeleteRequestDto.getRoomDealId());

        if (roomDealOptional.isPresent() && roomDealOptionOptional.isPresent()) {
            RoomDeal roomDeal = roomDealOptional.get();
            RoomDealOption roomDealOption = roomDealOptionOptional.get();
            // 본인 확인
            if (roomDeal.getMember().getId().equals(roomDealDeleteRequestDto.getMemberId())) {
                roomDealOptionReposiroty.deleteById(roomDealDeleteRequestDto.getRoomDealId());
                roomDealRepository.deleteById(roomDealDeleteRequestDto.getRoomDealId());
                return new RoomDealDeleteResponseDto(roomDealDeleteRequestDto.getRoomDealId());
            } else {
                throw new BaseException(BaseResponseStatus.NOT_AUTHORIZED);
            }
        } else {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
        }
    }

    /**
     * 주소로 매물 검색 + 본문 검색
     *
     * @param searchByAddressRequestDto
     * @return
     */
    public List<RoomDealSearchResponseDto> searchByAddress(SearchByAddressRequestDto searchByAddressRequestDto) {
        // match_phrase query 생성
        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("address", searchByAddressRequestDto.getAddress());

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder(); // Bool Query 생성
        ArrayList<QueryBuilder> queryBuilderList = new ArrayList<>(); // Bool Query 안에 넣을 query List 생성
        queryBuilderList.add(matchPhraseQuery); // match_phrase query를 list 안에 저장

        // 본문 검색
        if (!searchByAddressRequestDto.getContent().isEmpty()) {
            // term query 생성
            TermQueryBuilder termQuery = QueryBuilders.termQuery("content.nori", searchByAddressRequestDto.getContent());
            queryBuilderList.add(termQuery);
        }

        boolQueryBuilder.must().addAll(queryBuilderList); // Bool Query List를 Bool에 저장 => must : 조건 모두 일치

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder);

        // 결과 출력
        SearchHits<RoomDealSearchResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchResponseDto.class, IndexCoordinates.of("rooms_data"));

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealSearchResponseDto>> searchHitList = articles.getSearchHits();
        ArrayList<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = new ArrayList<>();
        for (SearchHit<RoomDealSearchResponseDto> item : searchHitList) {
            roomDealSearchResponseDtoList.add(item.getContent());
        }

        return roomDealSearchResponseDtoList;
    }

    /**
     * 위도, 경도로 매물 검색
     *
     * @param searchByStationUnivRequestDto
     * @return
     */
    public List<RoomDealSearchResponseDto> searchByLocation(SearchByStationUnivRequestDto searchByStationUnivRequestDto) {
        double lat = Double.parseDouble(searchByStationUnivRequestDto.getLat());
        double lon = Double.parseDouble(searchByStationUnivRequestDto.getLon());

        // geo_point query 생성
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(lat, lon)
                .distance(5, DistanceUnit.KILOMETERS);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder(); // Bool Query 생성
        ArrayList<QueryBuilder> queryBuilderList = new ArrayList<>(); // Bool Query 안에 넣을 query List 생성
        queryBuilderList.add(geoDistanceQueryBuilder); // match_phrase query를 list 안에 저장

        // 본문 검색
        if (!searchByStationUnivRequestDto.getContent().isEmpty()) {
            // term query 생성
            TermQueryBuilder termQuery = QueryBuilders.termQuery("content.nori", searchByStationUnivRequestDto.getContent());
            queryBuilderList.add(termQuery); // term query 저장
        }

        boolQueryBuilder.must().addAll(queryBuilderList); // Bool Query List를 Bool에 저장 => must : 조건 모두 일치

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // match_phrase query를 _search 안에 저장
        queryBuilder.withQuery(boolQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .geoDistanceSort("location", lat, lon) // 거리 기준 오름차순 정렬
                        .order(SortOrder.ASC)
                        .sortMode(SortMode.MIN))
                .withPageable(PageRequest.of(0, 100)).build(); // size 제한 (100개)

        // 결과 출력
        SearchHits<RoomDealSearchResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchResponseDto.class, IndexCoordinates.of("rooms_data"));

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealSearchResponseDto>> searchHitList = articles.getSearchHits();
        ArrayList<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = new ArrayList<>();
        for (SearchHit<RoomDealSearchResponseDto> item : searchHitList) {
            roomDealSearchResponseDtoList.add(item.getContent());
        }

        return roomDealSearchResponseDtoList;
    }

    /**
     * 본문 검색
     *
     * @param sentence
     * @return
     */
    public List<RoomDealSearchResponseDto> searchByContent(String sentence) {
        // match_phrase query 생성
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("content.nori", sentence);

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(matchQuery); // match_phrase query를 _search 안에 저장

        // 결과 출력
        SearchHits<RoomDealSearchResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchResponseDto.class, IndexCoordinates.of("rooms_data"));

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealSearchResponseDto>> searchHitList = articles.getSearchHits();
        ArrayList<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = new ArrayList<>();
        for (SearchHit<RoomDealSearchResponseDto> item : searchHitList) {
            roomDealSearchResponseDtoList.add(item.getContent());
        }

        return roomDealSearchResponseDtoList;
    }

    /**
     * 주소 기반으로 가까운 역, 대학교 검색
     *
     * @param searchByStationUnivRequestDto
     * @return
     */
    public RoomDealNearestStationUnivResponseDto getNearestStationUniv(SearchByStationUnivRequestDto searchByStationUnivRequestDto) {
        RoomDealNearestStationResponseDto roomDealNearestStationResponseDto = getNearestStation(searchByStationUnivRequestDto);
        RoomDealNearestUnivResponseDto roomDealNearestUnivResponseDto = getNearestUniv(searchByStationUnivRequestDto);

        RoomDealNearestStationUnivResponseDto roomDealNearestStationUnivResponseDto = new RoomDealNearestStationUnivResponseDto();

        roomDealNearestStationUnivResponseDto.setStationName(roomDealNearestStationResponseDto == null ? null : roomDealNearestStationResponseDto.getName());
        roomDealNearestStationUnivResponseDto.setUnivName(roomDealNearestUnivResponseDto == null ? null : roomDealNearestUnivResponseDto.getName());

        return roomDealNearestStationUnivResponseDto;
    }

    /**
     * 주소 API를 통해 위도, 경도 가져옴 => 가까운 역 찾기
     *
     * @param searchByStationUnivRequestDto
     */
    public RoomDealNearestStationResponseDto getNearestStation(SearchByStationUnivRequestDto searchByStationUnivRequestDto) {
        double lat = Double.parseDouble(searchByStationUnivRequestDto.getLat());
        double lon = Double.parseDouble(searchByStationUnivRequestDto.getLon());

        // geo_point query 생성
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(lat, lon)
                .distance(5, DistanceUnit.KILOMETERS);

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // match_phrase query를 _search 안에 저장
        queryBuilder.withQuery(geoDistanceQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .geoDistanceSort("location", lat, lon) // 거리 기준 오름차순 정렬
                        .order(SortOrder.ASC)
                        .sortMode(SortMode.MIN))
                .withPageable(PageRequest.of(0, 1)).build(); // size 제한 (1개)

        // 결과 출력
        SearchHits<RoomDealNearestStationResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealNearestStationResponseDto.class, IndexCoordinates.of("nearest_station_info"));

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealNearestStationResponseDto>> searchHitList = articles.getSearchHits();

        return searchHitList.isEmpty() ? null : searchHitList.get(0).getContent();
    }

    /**
     * 주소 API를 통해 위도, 경도 가져옴 => 가까운 대학교 찾기
     *
     * @param searchByStationUnivRequestDto
     */
    public RoomDealNearestUnivResponseDto getNearestUniv(SearchByStationUnivRequestDto searchByStationUnivRequestDto) {
        double lat = Double.parseDouble(searchByStationUnivRequestDto.getLat());
        double lon = Double.parseDouble(searchByStationUnivRequestDto.getLon());

        // geo_point query 생성
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(lat, lon)
                .distance(5, DistanceUnit.KILOMETERS);

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // match_phrase query를 _search 안에 저장
        queryBuilder.withQuery(geoDistanceQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .geoDistanceSort("location", lat, lon) // 거리 기준 오름차순 정렬
                        .order(SortOrder.ASC)
                        .sortMode(SortMode.MIN))
                .withPageable(PageRequest.of(0, 1)).build(); // size 제한 (1개)

        // 결과 출력
        SearchHits<RoomDealNearestUnivResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealNearestUnivResponseDto.class, IndexCoordinates.of("nearest_univ_info"));

        // 결과 => Document로 매핑
        List<SearchHit<RoomDealNearestUnivResponseDto>> searchHitList = articles.getSearchHits();

        return searchHitList.isEmpty() ? null : searchHitList.get(0).getContent();
    }

    /**
     * 검색어 목록 가져오기
     *
     * @param searchRelatedListRequestDto
     * @return
     */
    public List<SearchRelatedListUniteResponseDto> getSearchRelatedListFinal(SearchRelatedListRequestDto searchRelatedListRequestDto) {
        List<SearchRelatedListResponseDto> searchRelatedListResponseDtoList = getSearchRelatedList(searchRelatedListRequestDto);

        List<SearchRelatedListUniteResponseDto> searchRelatedListUniteResponseDtoList = new ArrayList<>();
        for (SearchRelatedListResponseDto s : searchRelatedListResponseDtoList) {
            if (s.getAddress() != null) {
                searchRelatedListUniteResponseDtoList.add(new SearchRelatedListUniteResponseDto(s.getAddress(), "address"));
                continue;
            }

            if (s.getStation() != null) {
                searchRelatedListUniteResponseDtoList.add(new SearchRelatedListUniteResponseDto(s.getStation(), "station"));
                continue;
            }

            searchRelatedListUniteResponseDtoList.add(new SearchRelatedListUniteResponseDto(s.getUniv(), "univ"));
        }

        return searchRelatedListUniteResponseDtoList;
    }

    /**
     * 주소, 역, 대학교 목록 가져오기
     *
     * @param searchRelatedListRequestDto
     * @return
     */
    public List<SearchRelatedListResponseDto> getSearchRelatedList(SearchRelatedListRequestDto searchRelatedListRequestDto) {
        String searchWord = searchRelatedListRequestDto.getSearchWord(); // 검색어

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder(); // Bool Query 생성
        ArrayList<QueryBuilder> queryBuilderList = new ArrayList<>(); // Bool Query 안에 넣을 query List 생성

        String[] searchWordArr = {"서울시", "부산시", "대구시", "대전시", "광주시", "울산시", "인천시", "강원도", "제주시"};
        String[] originWordArr = {"서울특별시", "부산 AND 광역시", "대구 AND 광역시", "대전 AND 광역시", "광주 AND 광역시", "울산 AND 광역시", "인천 AND 광역시", "강원 AND 특별자치도", "제주 AND 시"};

        HashMap<String, String> addressMap = new HashMap<>();
        for (int i = 0; i < searchWordArr.length; i++) {
            addressMap.put(searchWordArr[i], originWordArr[i]);
        }

        /* 주소 Query 생성 */
        if (!searchWord.contains("학교")) {
            StringTokenizer stk = new StringTokenizer(searchWord);
            StringBuilder sb = new StringBuilder();

            // 첫번째 단어 확인 (시, 도, 구, 군, 동, 읍, 면, 리)
            String firstWord = stk.nextToken();
            if (addressMap.containsKey(firstWord)) {
                sb.append(addressMap.get(firstWord));
            } else if (checkGuGunDongEupMyeonLi(firstWord)) {
                sb.append(firstWord, 0, firstWord.length() - 1)
                        .append(" AND ").append(firstWord.substring(firstWord.length() - 1));
            } else {
                sb.append(firstWord);
            }

            // 조건 확인
            while (stk.hasMoreTokens()) {
                String cur = stk.nextToken();
                if (checkGuGunDongEupMyeonLi(cur)) {
                    sb.append(" AND ").append(cur, 0, cur.length() - 1)
                            .append(" AND ").append(cur.substring(cur.length() - 1));
                } else {
                    sb.append(" AND ").append(cur);
                }
            }

            String newWord = sb.toString();

            QueryStringQueryBuilder queryStringQuery = QueryBuilders.queryStringQuery(newWord);
//            MatchQueryBuilder matchAddressQuery = QueryBuilders.matchQuery("address.nori", searchWord); // match query 생성
            queryBuilderList.add(queryStringQuery); // match query를 list 안에 저장
        }

        /* 역 Query 생성 */
        TermQueryBuilder termStationQuery = QueryBuilders.termQuery("station.nori", searchWord); // term query 생성
        queryBuilderList.add(termStationQuery); // term query를 list 안에 저장

        if (searchWord.contains("학교")) { /* 대학교 Query 생성 */
            MatchQueryBuilder matchUnivQuery = QueryBuilders.matchQuery("univ.nori", searchWord); // match query 생성
            queryBuilderList.add(matchUnivQuery); // match query를 list 안에 저장
        } else if (searchWord.endsWith("대")) {
            MatchQueryBuilder matchUnivQuery = QueryBuilders.matchQuery("univ.nori", searchWord.substring(0, searchWord.length() - 1));
            queryBuilderList.add(matchUnivQuery);
        }

        boolQueryBuilder.should().addAll(queryBuilderList); // Bool Query List를 Bool에 저장 => should : or

        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .fieldSort("sortId") // 거리 기준 오름차순 정렬
                        .order(SortOrder.ASC))
                .withPageable(PageRequest.of(0, 40)).build(); // size 제한 (20개);

        // 결과 출력
        SearchHits<SearchRelatedListResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), SearchRelatedListResponseDto.class, IndexCoordinates.of("search_related_list"));

        // 결과 => Document로 매핑
        List<SearchHit<SearchRelatedListResponseDto>> searchHitList = articles.getSearchHits();
        ArrayList<SearchRelatedListResponseDto> searchListResponseDtoRelatedList = new ArrayList<>();
        for (SearchHit<SearchRelatedListResponseDto> item : searchHitList) {
            searchListResponseDtoRelatedList.add(item.getContent());
        }

        return searchListResponseDtoRelatedList;
    }

    /**
     * 구군 동읍면리 체크
     *
     * @param word
     * @return
     */
    public boolean checkGuGunDongEupMyeonLi(String word) {
        if ((word.endsWith("구") || word.endsWith("군") ||
                word.endsWith("동") || word.endsWith("읍") || word.endsWith("면") || word.endsWith("리"))
                && word.length() >= 3) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Cache에 있는 RoomDeal 가져오기
     * @param roomDealSearchDtoList
     * @return List<RoomDeal>
     * @throws BaseException
     */
    @Cacheable(value="address", key = "#roomDealSearchDtoList", cacheManager = "cacheManager")
    public List<RoomDeal> getRoomDealByIdAtCache(List<RoomDealSearchResponseDto> roomDealSearchDtoList) throws BaseException {
        List<RoomDeal> roomDeals = new ArrayList<>();

        for (RoomDealSearchResponseDto roomDealSearchDto : roomDealSearchDtoList) {
            Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealSearchDto.getRoomId());
            if (roomDealOptional.isEmpty()) {
                throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
            }

            roomDeals.add(roomDealOptional.get());
        }

        return roomDeals;

    }
}
