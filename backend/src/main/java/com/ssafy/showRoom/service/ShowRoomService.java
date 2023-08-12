package com.ssafy.showRoom.service;

import com.ssafy.elasticsearch.dto.*;
import com.ssafy.elasticsearch.repository.ShowRoomElasticSearchRepository;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.member.domain.Member;
import com.ssafy.member.repository.MemberRepository;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.repository.RoomDealRepository;
import com.ssafy.showRoom.domain.ShowRoom;
import com.ssafy.showRoom.dto.ShowRoomRegisterRequestDto;
import com.ssafy.showRoom.dto.ShowRoomResponseDto;
import com.ssafy.showRoom.repository.ShowRoomRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.search.MatchQuery;
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

import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShowRoomService {

    private final ElasticsearchOperations elasticsearchOperations;

    private final ShowRoomRepository showRoomRepository;
    private final RoomDealRepository roomDealRepository;
    private final MemberRepository memberRepository;
    private final ShowRoomElasticSearchRepository showRoomElasticSearchRepositor;


    public ShowRoomResponseDto registerShowRoom(ShowRoomRegisterRequestDto showRoomRegisterRequestDto) throws BaseException {

        RoomDeal roomDeal = verifyRoomDeal(showRoomRegisterRequestDto.getRoomDealId());

        checkMemberAuthority(roomDeal, showRoomRegisterRequestDto.getMemberId());

        Member member = verifyMember(showRoomRegisterRequestDto.getMemberId());

        ShowRoom showRoom = ShowRoom.builder()
                .roomDeal(roomDeal)
                .member(member)
                .roadAddress(roomDeal.getRoadAddress())
                .jibunAddress(roomDeal.getJibunAddress())
                .station(roomDeal.getStation())
                .univ(roomDeal.getUniv())
                .registerTime(new Date(System.currentTimeMillis()))
                .dealStatus(roomDeal.getDealStatus())
                .build();

        showRoomRepository.save(showRoom);

        String id = String.valueOf(showRoom.getId());
        Integer showRoomId = showRoom.getId();
        String address = showRoom.getJibunAddress();
        String station = showRoom.getStation();
        String univ = showRoom.getUniv();
        String hashTag = ""; // --------------- 해시태그 완성되어야 함 ---------------
        String registerTime = showRoom.getRegisterTime().toString();

        ShowRoomSaveDto showRoomSaveDto = new ShowRoomSaveDto(id, showRoomId, address, station, univ, hashTag, registerTime);

        try {
            showRoomElasticSearchRepositor.save(showRoomSaveDto);
        } catch (Exception e) {
            return new ShowRoomResponseDto(showRoom);
        }

        return new ShowRoomResponseDto(showRoom);
    }

    private RoomDeal verifyRoomDeal(Long roomDealId) {
        Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealId);
        if (roomDealOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 매물입니다.");
        }

        return roomDealOptional.get();
    }

    private void checkMemberAuthority(RoomDeal roomDeal, UUID memberId) {
        if (!roomDeal.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("권한이 없는 사용자 입니다.");
        }
    }

    private Member verifyMember(UUID memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다.");
        }
        return memberOptional.get();
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
     * 곰방봐 검색 결과
     * 
     * @param showRoomSearchRequestDto
     * @return
     */
    public List<ShowRoomSearchResponseDto> getSearchResult(ShowRoomSearchRequestDto showRoomSearchRequestDto) {
        String searchType = showRoomSearchRequestDto.getSearchType();

        List<ShowRoomSearchResponseDto> showRoomSearchResponseDtoList = new ArrayList<>();
        if (searchType.equals("total")) {
            showRoomSearchResponseDtoList = getSearchTotalResult(showRoomSearchRequestDto);
        } else {
            showRoomSearchResponseDtoList = getSearchNotTotalResult(showRoomSearchRequestDto);
        }

        return showRoomSearchResponseDtoList;
    }

    /**
     * 해시태그 Query 생성
     * 
     * @param hashTag
     * @return
     */
    public ArrayList<QueryBuilder> makeMatchQuery(String hashTag) {
        ArrayList<QueryBuilder> queryBuilderList = new ArrayList<>(); // Bool Query 안에 넣을 query List 생성

        StringTokenizer stk = new StringTokenizer(hashTag);
        while (stk.hasMoreTokens()) {
            // match query 생성
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("hashTag.nori", stk.nextToken());
            queryBuilderList.add(matchQuery);
        }

        return queryBuilderList;
    }

    /**
     * Search Query 생성
     *
     * @param boolQueryBuilder
     * @param pageOffset
     * @param sortType
     * @return
     */
    public ArrayList<ShowRoomSearchResponseDto> makeSearchQuery(BoolQueryBuilder boolQueryBuilder, int pageOffset, String sortType) {
        /* _search query 생성 */
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder)
                .withSort(SortBuilders // sort builder
                        .fieldSort("registerTime") // 등록 시간 기준 오름차순 정렬
                        .order(sortType.equals("asc") ? SortOrder.ASC : SortOrder.DESC))
                .withPageable(PageRequest.of(pageOffset, 30)).build(); // size 제한 (30개);

        /* 결과 가져오기 */
        SearchHits<ShowRoomSearchResponseDto> articles = elasticsearchOperations
                .search(queryBuilder.build(), ShowRoomSearchResponseDto.class, IndexCoordinates.of("showroom_data"));

        /* 결과 => Document로 매핑 */
        List<SearchHit<ShowRoomSearchResponseDto>> searchHitList = articles.getSearchHits();
        ArrayList<ShowRoomSearchResponseDto> showRoomSearchResponseDtoList = new ArrayList<>();
        for (SearchHit<ShowRoomSearchResponseDto> item : searchHitList) {
            showRoomSearchResponseDtoList.add(item.getContent());
        }

        return showRoomSearchResponseDtoList;
    }
    
    /**
     * 전체 검색 결과 + 해시태그 + 정렬
     * 
     * @param showRoomSearchRequestDto
     * @return
     */
    public List<ShowRoomSearchResponseDto> getSearchTotalResult(ShowRoomSearchRequestDto showRoomSearchRequestDto) {
        String hashTag = showRoomSearchRequestDto.getHashTag();
        String sortType = showRoomSearchRequestDto.getSortType();
        int pageOffset = showRoomSearchRequestDto.getPageOffset();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder(); // Bool Query 생성
        ArrayList<QueryBuilder> queryBuilderList = new ArrayList<>(); // Bool Query 안에 넣을 query List 생성

        /* 해시태그 검색 */
        if (!hashTag.isEmpty()) {
            queryBuilderList = makeMatchQuery(hashTag);
        }

        boolQueryBuilder.must().addAll(queryBuilderList); // Bool Query List를 Bool에 저장 => must : 조건 모두 일치

        return makeSearchQuery(boolQueryBuilder, pageOffset, sortType);
    }

    /**
     * 검색어 + 검색어 유형 + 해시태그 + 정렬
     *
     * @param showRoomSearchRequestDto
     * @return
     */
    public List<ShowRoomSearchResponseDto> getSearchNotTotalResult(ShowRoomSearchRequestDto showRoomSearchRequestDto) {
        String searchWord = showRoomSearchRequestDto.getSearchWord();
        String searchType = showRoomSearchRequestDto.getSearchType();
        String hashTag = showRoomSearchRequestDto.getHashTag();
        String sortType = showRoomSearchRequestDto.getSortType();
        int pageOffset = showRoomSearchRequestDto.getPageOffset();

        // match_phrase query 생성 (검색 유형 체크)
        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery(searchType, searchWord);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder(); // Bool Query 생성
        ArrayList<QueryBuilder> queryBuilderList = new ArrayList<>(); // Bool Query 안에 넣을 query List 생성

        /* 해시태그 검색 */
        if (!hashTag.isEmpty()) {
            queryBuilderList = makeMatchQuery(hashTag);
        }

        queryBuilderList.add(matchPhraseQuery); // match_phrase query를 list 안에 저장
        boolQueryBuilder.must().addAll(queryBuilderList); // Bool Query List를 Bool에 저장 => must : 조건 모두 일치

        return makeSearchQuery(boolQueryBuilder, pageOffset, sortType);
    }
}
