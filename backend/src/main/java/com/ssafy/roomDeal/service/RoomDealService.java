package com.ssafy.roomDeal.service;

import com.ssafy.elasticsearch.dto.*;
import com.ssafy.elasticsearch.repository.RoomDealElasticSearchRepository;
import com.ssafy.elasticsearch.repository.ShowRoomElasticSearchRepository;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.likeShowRoom.repository.LikeShowRoomRepository;
import com.ssafy.member.domain.Member;
import com.ssafy.member.repository.MemberRepository;
import com.ssafy.redis.RoomDealRedisStoreDto;
import com.ssafy.redis.entity.RoomDealInfo;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.domain.RoomDealImageInfo;
import com.ssafy.roomDeal.domain.RoomDealOption;
import com.ssafy.roomDeal.dto.*;
import com.ssafy.roomDeal.repository.RoomDealImageInfoRepository;
import com.ssafy.roomDeal.repository.RoomDealOptionReposiroty;
import com.ssafy.redis.repository.RoomDealRedisRepository;
import com.ssafy.roomDeal.repository.RoomDealRepository;
import com.ssafy.showRoom.domain.ShowRoom;
import com.ssafy.showRoom.repository.ShowRoomRepository;
import com.ssafy.showRoomHashTag.domain.ShowRoomHashTag;
import com.ssafy.showRoomHashTag.domain.ShowRoomHashTagId;
import com.ssafy.showRoomHashTag.repository.ShowRoomHashTagRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
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
    private final RoomDealImageInfoRepository roomDealImageInfoRepository;
    private final ShowRoomRepository showRoomRepository;
    private final ShowRoomHashTagRepository showRoomHashTagRepository;
    private final LikeShowRoomRepository likeShowRoomRepository;
    private final ShowRoomElasticSearchRepository showRoomElasticSearchRepository;

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
        LocationRequestDto locationRequestDto = new LocationRequestDto(newRoomDeal.getLat(), newRoomDeal.getLon());
        String content = newRoomDeal.getContent();

        RoomDealSaveDto roomDealSaveDto = new RoomDealSaveDto(id, roomId, address, locationRequestDto, content);

        try {
            roomDealElasticSearchRepository.save(roomDealSaveDto);
        } catch (Exception e) {
            return new RoomDealResponseDto(newRoomDeal, newRoomDealOption);
        }

        return new RoomDealResponseDto(newRoomDeal, newRoomDealOption);
    }

    // 매믈 조회
    public RoomDealResponseDto getRoomDeal(Long id) throws BaseException {
        Optional<RoomDeal> roomDeal = roomDealRepository.findById(id);
        Optional<RoomDealOption> roomDealOption = roomDealOptionReposiroty.findById(id);

        List<RoomDealImageInfo> roomDealImageInfoList = roomDealImageInfoRepository.findAllByRoomDealId(id);
        List<String> roomDealImageUrls = new ArrayList<>();
        for (RoomDealImageInfo imageInfo : roomDealImageInfoList) {
            roomDealImageUrls.add(imageInfo.getFileUrl());
        }

        if (roomDeal.isPresent() && roomDealOption.isPresent()) {
            return new RoomDealResponseDto(roomDeal.get(), roomDealOption.get(), roomDealImageUrls);
        } else {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
        }
    }

    // 매물 수정
    @Transactional
    public RoomDealResponseDto updateRoomDeal(RoomDealUpdateRequestDto roomDealUpdateRequestDto) throws BaseException {
        Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealUpdateRequestDto.getRoomDealId());
        Optional<RoomDealOption> roomDealOptionOptional = roomDealOptionReposiroty.findById(roomDealUpdateRequestDto.getRoomDealId());

        List<RoomDealImageInfo> roomDealImageInfoList = roomDealImageInfoRepository.findAllByRoomDealId(roomDealUpdateRequestDto.getRoomDealId());
        List<String> roomDealImageUrls = new ArrayList<>();
        for (RoomDealImageInfo imageInfo : roomDealImageInfoList) {
            roomDealImageUrls.add(imageInfo.getFileUrl());
        }

        if (roomDealOptional.isPresent() && roomDealOptionOptional.isPresent()) {
            RoomDeal roomDeal = roomDealOptional.get();

            // 본인 확인
            if (roomDeal.getMember().getId().equals(roomDealUpdateRequestDto.getMemberId())) {
                roomDeal.roomDealUpdate(roomDealUpdateRequestDto);
                return new RoomDealResponseDto(roomDealOptional.get(), roomDealOptionOptional.get(), roomDealImageUrls);
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
                deleteShowRoom(roomDeal.getId(), roomDeal.getMember().getId());

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
     * 곰방봐 삭제
     *
     * @param roomDealId
     * @param memberId
     * @throws BaseException
     */
    @Transactional
    public void deleteShowRoom(Long roomDealId, UUID memberId) throws BaseException {
        ShowRoom showRoom = showRoomRepository.findByRoomDealId(roomDealId);

        // showRoom 등록 여부 확인
        if (showRoom == null) {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_SHOW_ROOM_ID);
        }

        Integer showRoomId = showRoom.getId();
        List<Integer> showRoomHashTagIdList = showRoomHashTagRepository.findByShowRoomId(showRoomId);

        // 본인 확인
        if (!showRoom.getMember().getId().equals(memberId)) {
            throw new BaseException(BaseResponseStatus.NOT_AUTHORIZED);
        }

        // showRoomHashTag에서 값 삭제
        for (Integer hashTagId : showRoomHashTagIdList) {
            ShowRoomHashTagId showRoomHashTagId = new ShowRoomHashTagId(showRoomId, hashTagId);
            Optional<ShowRoomHashTag> showRoomHashTagOptional = showRoomHashTagRepository.findById(showRoomHashTagId);

            // showRoomHashTag 등록 여부 확인
            if (!showRoomHashTagOptional.isPresent()) {
                throw new BaseException(BaseResponseStatus.NOT_MATCHED_SHOW_ROOM_HASH_TAG_ID);
            }

            showRoomHashTagRepository.deleteById(showRoomHashTagId);
        }

        likeShowRoomRepository.deleteByShowRoomId(showRoomId); // 좋아요 삭제
        showRoomRepository.deleteById(showRoomId); // showRoom 삭제

        try {
            showRoomElasticSearchRepository.deleteById(showRoomId.toString()); // Elastic Search에 저장된 ShowRoom 삭제
        } catch (Exception e) {
            return ;
        }
    }

    @Transactional
    public void saveImages(List<String> files, RoomDealResponseDto roomDealResponseDto) {
        roomDealResponseDto.addFileUrls(files);

        roomDealResponseDto.getRoomDeal().saveThumbnail(files.get(0));
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

        /* 본문 검색 */
        if (!searchByAddressRequestDto.getContent().isEmpty()) {
            // term query 생성
            TermQueryBuilder termQuery = QueryBuilders.termQuery("content.nori", searchByAddressRequestDto.getContent());
            queryBuilderList.add(termQuery);
        }

        boolQueryBuilder.must().addAll(queryBuilderList); // Bool Query List를 Bool에 저장 => must : 조건 모두 일치

        /* _search query 생성 */
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder);

        /* 결과 가져오기 */
        SearchHits<RoomDealSearchResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchResponseDto.class, IndexCoordinates.of("rooms_data"));

        /* 결과 => Document로 매핑 */
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

        /* geo_point query 생성 */
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(lat, lon)
                .distance(5, DistanceUnit.KILOMETERS);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder(); // Bool Query 생성
        ArrayList<QueryBuilder> queryBuilderList = new ArrayList<>(); // Bool Query 안에 넣을 query List 생성
        queryBuilderList.add(geoDistanceQueryBuilder); // match_phrase query를 list 안에 저장

        /* 본문 검색 */
        if (!searchByStationUnivRequestDto.getContent().isEmpty()) {
            // term query 생성
            TermQueryBuilder termQuery = QueryBuilders.termQuery("content.nori", searchByStationUnivRequestDto.getContent());
            queryBuilderList.add(termQuery); // term query 저장
        }

        boolQueryBuilder.must().addAll(queryBuilderList); // Bool Query List를 Bool에 저장 => must : 조건 모두 일치

        /* _search query 생성 */
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        /* match_phrase query를 _search 안에 저장 */
        queryBuilder.withQuery(boolQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .geoDistanceSort("location", lat, lon) // 거리 기준 오름차순 정렬
                        .order(SortOrder.ASC)
                        .sortMode(SortMode.MIN))
                .withPageable(PageRequest.of(0, 100)).build(); // size 제한 (100개)

        /* 결과 가져오기 */
        SearchHits<RoomDealSearchResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), RoomDealSearchResponseDto.class, IndexCoordinates.of("rooms_data"));

        /* 결과 => Document로 매핑 */
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

        // 결과 가져오기
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
     * @param locationRequestDto
     * @return
     */
    public RoomDealNearestStationUnivResponseDto getNearestStationUniv(LocationRequestDto locationRequestDto) {
        RoomDealNearestStationResponseDto roomDealNearestStationResponseDto = getNearestStation(locationRequestDto); // 가까운 역
        RoomDealNearestUnivResponseDto roomDealNearestUnivResponseDto = getNearestUniv(locationRequestDto); // 가까운 대학교

        RoomDealNearestStationUnivResponseDto roomDealNearestStationUnivResponseDto = new RoomDealNearestStationUnivResponseDto();

        roomDealNearestStationUnivResponseDto.setStationName(roomDealNearestStationResponseDto == null ? null : roomDealNearestStationResponseDto.getName());
        roomDealNearestStationUnivResponseDto.setUnivName(roomDealNearestUnivResponseDto == null ? null : roomDealNearestUnivResponseDto.getName());

        return roomDealNearestStationUnivResponseDto;
    }

    /**
     * 가까운 역, 대학교 검색 Query 생성
     *
     * @param locationRequestDto
     * @return
     */
    public NativeSearchQueryBuilder makeNearestQueryBuilder(LocationRequestDto locationRequestDto) {
        double lat = Double.parseDouble(locationRequestDto.getLat()); // 위도
        double lon = Double.parseDouble(locationRequestDto.getLon()); // 경도

        /* geo_point query 생성 */
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(lat, lon)
                .distance(5, DistanceUnit.KILOMETERS);

        /* _search query 생성 */
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        /* match_phrase query를 _search query 안에 저장 */
        queryBuilder.withQuery(geoDistanceQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .geoDistanceSort("location", lat, lon) // 거리 기준 오름차순 정렬
                        .order(SortOrder.ASC)
                        .sortMode(SortMode.MIN))
                .withPageable(PageRequest.of(0, 1)).build(); // size 제한 (1개)

        return queryBuilder;
    }

    /**
     * 주소 API를 통해 위도, 경도 가져옴 => 가까운 역 찾기
     *
     * @param locationRequestDto
     */
    public RoomDealNearestStationResponseDto getNearestStation(LocationRequestDto locationRequestDto) {
        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = makeNearestQueryBuilder(locationRequestDto);

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
     * @param locationRequestDto
     */
    public RoomDealNearestUnivResponseDto getNearestUniv(LocationRequestDto locationRequestDto) {
        // _search query 생성
        NativeSearchQueryBuilder queryBuilder = makeNearestQueryBuilder(locationRequestDto);

        // 결과 가져오기
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
            /* 주소 매핑 */
            if (s.getAddress() != null) {
                searchRelatedListUniteResponseDtoList.add(new SearchRelatedListUniteResponseDto(s.getAddress(), "address"));
                continue;
            }

            /* 역 매핑 */
            if (s.getStation() != null) {
                searchRelatedListUniteResponseDtoList.add(new SearchRelatedListUniteResponseDto(s.getStation(), "station"));
                continue;
            }

            /* 대학교 매핑 */
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

        /* 시 매핑 */
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

            /* 첫번째 단어 확인 (시, 도, 구, 군, 동, 읍, 면, 리) */
            String firstWord = stk.nextToken();
            if (addressMap.containsKey(firstWord)) {
                sb.append(addressMap.get(firstWord));
            } else if (checkGuGunDongEupMyeonLi(firstWord)) {
                sb.append(firstWord, 0, firstWord.length() - 1)
                        .append(" AND ").append(firstWord.substring(firstWord.length() - 1));
            } else {
                sb.append(firstWord);
            }

            /* 조건 확인 (구, 군, 동, 읍, 면, 리) */
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

            /* query_string 생성 */
            QueryStringQueryBuilder queryStringQuery = QueryBuilders.queryStringQuery(newWord);
            queryBuilderList.add(queryStringQuery);
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
     *
     * @param roomDealSearchDtoList
     * @return List<RoomDeal>
     * @throws BaseException
     */
    public List<RoomDealListResponseDto> getRoomDealByIdAtCache(List<RoomDealSearchResponseDto> roomDealSearchDtoList, String address) throws BaseException {
        List<RoomDealListResponseDto> roomDealListResponseDtolist = new ArrayList<>();
        List<RoomDealRedisStoreDto> roomDealRedisStoreDtoList = new ArrayList<>();
        for (RoomDealSearchResponseDto roomDealSearchDto : roomDealSearchDtoList) {
            Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealSearchDto.getRoomId());
            if (roomDealOptional.isEmpty()) {
                throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
            }

            Optional<RoomDealOption> roomDealOptionOptional = roomDealOptionReposiroty.findById(roomDealSearchDto.getRoomId());
            if (roomDealOptionOptional.isEmpty()) {
                throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_OPTION_ID);
            }

            roomDealRedisStoreDtoList.add(new RoomDealRedisStoreDto(roomDealOptionOptional.get()));
            // 이걸 dto로 생성해서 넣어야됨
            roomDealListResponseDtolist.add(new RoomDealListResponseDto(roomDealOptional.get()));
        }
        RoomDealInfo roomDealInfo = new RoomDealInfo(address, roomDealRedisStoreDtoList);
        roomDealRedisRepository.save(roomDealInfo);

        return roomDealListResponseDtolist;

    }

    /**
     * 2차검색 + 필터링
     *
     * @param filteredRoomDealListRequestDto
     * @throws BaseException
     */
    public List<RoomDealListResponseDto> filterRoomDeal(FilteredRoomDealListRequestDto filteredRoomDealListRequestDto) throws BaseException {

        List<RoomDealListResponseDto> roomDealListResponseDtoList = new ArrayList<>();

        Optional<RoomDealInfo> roomDealInfoOptional = roomDealRedisRepository.findById(filteredRoomDealListRequestDto.getAddress());
        if (roomDealInfoOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
        }


        List<RoomDealRedisStoreDto> roomDealRedisStoreDtoList = roomDealInfoOptional.get().getRoomDealRedisStoreDtoList();


        for (RoomDealRedisStoreDto searchResult : roomDealRedisStoreDtoList) {
            // 필터링이랑 일치하면 리스트에 담아서 리턴

            // RoomType 전체가 아니고, 원하는 타입이 아닐 경우 continue
            if (!filteredRoomDealListRequestDto.getRoomType().contains("전체")) {
                if (!filteredRoomDealListRequestDto.getRoomType().contains(searchResult.getRoomType())) {
                    continue;
                }
            }

            // OneroomType 전체가 아니고, 원하는 방구조가 아닐 경우 continue
            if (!filteredRoomDealListRequestDto.getOneroomType().contains("전체")) {
                if (!filteredRoomDealListRequestDto.getOneroomType().contains(searchResult.getOneroomType())) {
                    continue;
                }
            }

            // Floor 최저층보다 낮거나, 최고층보다 높으면 continue
            if (filteredRoomDealListRequestDto.getStartFloor() > searchResult.getFloor()
                    || filteredRoomDealListRequestDto.getEndFloor() < searchResult.getFloor()) {
                continue;
            }

            // MonthlyFee 최저월세보다 낮거나, 최고월세보다 높으면 continue
            if (filteredRoomDealListRequestDto.getStartMonthlyFee() > searchResult.getMonthlyFee()
                    || filteredRoomDealListRequestDto.getEndMonthlyFee() < searchResult.getMonthlyFee()) {
                continue;
            }

            // Deposit 최저보증금보다 낮거나, 최고보증금보다 높으면 continue
            if (filteredRoomDealListRequestDto.getStartDeposit() > searchResult.getDeposit()
                    || filteredRoomDealListRequestDto.getEndDeposit() < searchResult.getDeposit()) {
                continue;
            }

            // ManagementFee 최저관리비보다 낮거나, 최고관리비보다 높으면 continue
            if (filteredRoomDealListRequestDto.getStartManagementFee() > searchResult.getManagementFee()
                    || filteredRoomDealListRequestDto.getEndManagementFee() < searchResult.getManagementFee()) {
                continue;
            }

            // RoomSize 최저방크기보다 작거나, 최고방크기보다 크면 continue
            if (filteredRoomDealListRequestDto.getStartRoomSize() > searchResult.getRoomSize()
                    || filteredRoomDealListRequestDto.getEndRoomSize() < searchResult.getRoomSize()) {
                continue;
            }

            if (filteredRoomDealListRequestDto.isAirConditioner() && !searchResult.isAirConditioner()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isRefrigerator() && !searchResult.isRefrigerator()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isWasher() && !searchResult.isWasher()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isDryer() && !searchResult.isDryer()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isSink() && !searchResult.isSink()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isGasRange() && !searchResult.isGasRange()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isCloset() && !searchResult.isCloset()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isShoeCloset() && !searchResult.isCloset()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isFireAlarm() && !searchResult.isFireAlarm()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isElevator() && !searchResult.isElevator()) {
                continue;
            }
            if (filteredRoomDealListRequestDto.isParkingLot() && !searchResult.isParkingLot()) {
                continue;
            }

            roomDealListResponseDtoList.add(new RoomDealListResponseDto(searchResult));

        }

        return roomDealListResponseDtoList;
    }
}
