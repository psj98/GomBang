package com.ssafy.showRoom.service;

import com.ssafy.elasticsearch.dto.*;
import com.ssafy.elasticsearch.repository.ShowRoomElasticSearchRepository;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.hashTag.domain.HashTag;
import com.ssafy.hashTag.dto.HashTagRegisterRequestDto;
import com.ssafy.hashTag.repository.HashTagRepository;
import com.ssafy.likeShowRoom.domain.LikeShowRoomId;
import com.ssafy.likeShowRoom.repository.LikeShowRoomRepository;
import com.ssafy.member.domain.Member;
import com.ssafy.member.repository.MemberRepository;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.repository.RoomDealRepository;
import com.ssafy.showRoom.domain.ShowRoom;
import com.ssafy.showRoom.domain.ShowRoomImageInfo;
import com.ssafy.showRoom.dto.*;
import com.ssafy.showRoom.repository.ShowRoomImageInfoRepository;
import com.ssafy.showRoom.repository.ShowRoomRepository;
import com.ssafy.showRoomHashTag.domain.ShowRoomHashTag;
import com.ssafy.showRoomHashTag.domain.ShowRoomHashTagId;
import com.ssafy.showRoomHashTag.dto.ShowRoomHashTagRequestDto;
import com.ssafy.showRoomHashTag.repository.ShowRoomHashTagRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShowRoomService {

    private final ElasticsearchOperations elasticsearchOperations;

    private final ShowRoomRepository showRoomRepository;
    private final RoomDealRepository roomDealRepository;
    private final MemberRepository memberRepository;
    private final HashTagRepository hashTagRepository;
    private final ShowRoomHashTagRepository showRoomHashTagRepository;
    private final LikeShowRoomRepository likeShowRoomRepository;
    private final ShowRoomElasticSearchRepository showRoomElasticSearchRepository;
    private final ShowRoomImageInfoRepository showRoomImageInfoRepository;

    /**
     * 곰방봐 등록 + 해시태그 등록 + ElasticSearch에 값 저장
     *
     * @param showRoomHashTagRequestDto
     * @return
     * @throws BaseException
     */
    @Transactional
    public ShowRoomResponseDto registerShowRoom(ShowRoomHashTagRequestDto showRoomHashTagRequestDto) throws BaseException {
        ShowRoomRegisterRequestDto showRoomRegisterRequestDto = showRoomHashTagRequestDto.getShowRoomRegisterRequestDto();
        HashTagRegisterRequestDto hashTagRegisterRequestDto = showRoomHashTagRequestDto.getHashTagRegisterRequestDto();

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

        /* 해시태그, 중간 테이블 저장 */
        StringBuilder sb = new StringBuilder();
        List<String> hashTagList = hashTagRegisterRequestDto.getHashTagNames(); // 해시태그

        for (String cur : hashTagList) {
            int check = hashTagRepository.exist(cur); // 기존 테이블에 해시태그 있는지 체크

            HashTag hashTag = HashTag.builder()
                    .hashTagName(cur)
                    .build();

            if (check != 0) { // 해시태그가 있는 경우, 기존 해시태그 id를 가져와서 저장
                int hashTagId = hashTagRepository.getHashTagId(cur);
                hashTag.setId(hashTagId);
            } else { // 해시태그가 없는 경우, 저장
                hashTagRepository.save(hashTag);
            }

            // 복합키 생성
            ShowRoomHashTagId showRoomHashTagId = new ShowRoomHashTagId(showRoom.getId(), hashTag.getId());

            // 중간 테이블 생성 및 저장
            ShowRoomHashTag showRoomHashTag = ShowRoomHashTag.builder()
                    .id(showRoomHashTagId)
                    .showRoom(showRoom)
                    .hashTag(hashTag)
                    .build();

            showRoomHashTagRepository.save(showRoomHashTag);

            sb.append(cur).append(" "); // ElasticSearch에 저장할 String 생성
        }

        /* Elastic Search에 저장 */
        String id = String.valueOf(showRoom.getId());
        Integer showRoomId = showRoom.getId();
        String address = showRoom.getJibunAddress();
        String station = showRoom.getStation();
        String univ = showRoom.getUniv();
        String hashTag = sb.toString();
        String registerTime = showRoom.getRegisterTime().toString();

        ShowRoomSaveDto showRoomSaveDto = new ShowRoomSaveDto(id, showRoomId, address, station, univ, hashTag, registerTime);

        try {
            showRoomElasticSearchRepository.save(showRoomSaveDto);
        } catch (Exception e) {
            return new ShowRoomResponseDto(showRoom);
        }

        return new ShowRoomResponseDto(showRoom);
    }

    /**
     * 매물 id 체크
     *
     * @param roomDealId
     * @return
     * @throws BaseException
     */
    private RoomDeal verifyRoomDeal(Long roomDealId) throws BaseException {
        Optional<RoomDeal> roomDealOptional = roomDealRepository.findById(roomDealId);
        if (roomDealOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_ROOM_DEAL_ID);
        }

        return roomDealOptional.get();
    }

    /**
     * 매물을 등록한 memberId와 로그인 한 유저의 memberId 체크
     *
     * @param roomDeal
     * @param memberId
     * @throws BaseException
     */
    private void checkMemberAuthority(RoomDeal roomDeal, UUID memberId) throws BaseException {
        if (!roomDeal.getMember().getId().equals(memberId)) {
            throw new BaseException(BaseResponseStatus.NOT_AUTHORIZED);
        }
    }

    /**
     * memberId 체크
     *
     * @param memberId
     * @return
     * @throws BaseException
     */
    private Member verifyMember(UUID memberId) throws BaseException {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER);
        }
        return memberOptional.get();
    }

    /**
     * 곰방봐 삭제
     *
     * @param showRoomDeleteRequestDto
     * @return
     * @throws BaseException
     */
    @Transactional
    public ShowRoomDeleteResponseDto deleteShowRoom(ShowRoomDeleteRequestDto showRoomDeleteRequestDto) throws BaseException {
        Integer showRoomId = showRoomDeleteRequestDto.getShowRoomId();
        UUID memberId = showRoomDeleteRequestDto.getMemberId();

        Optional<ShowRoom> showRoomOptional = showRoomRepository.findById(showRoomId);
        List<Integer> showRoomHashTagIdList = showRoomHashTagRepository.findByShowRoomId(showRoomId);

        // showRoom 등록 여부 확인
        if (!showRoomOptional.isPresent()) {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_SHOW_ROOM_ID);
        }

        ShowRoom showRoom = showRoomOptional.get();

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
            return new ShowRoomDeleteResponseDto(showRoomId);
        }

        return new ShowRoomDeleteResponseDto(showRoomId);
    }

    /**
     * 곰방봐 상세보기
     *
     * @param showRoomDetailRequestDto
     * @return
     * @throws BaseException
     */
    public ShowRoomDetailResponseDto getShowRoom(ShowRoomDetailRequestDto showRoomDetailRequestDto) throws BaseException {
        UUID memberId = showRoomDetailRequestDto.getMemberId();
        Integer showRoomId = showRoomDetailRequestDto.getShowRoomId();

        Optional<ShowRoom> showRoom = showRoomRepository.findById(showRoomId);

        if (!showRoom.isPresent()) {
            throw new BaseException(BaseResponseStatus.NOT_MATCHED_SHOW_ROOM_ID);
        }

        // 이미지 가져오기
        List<ShowRoomImageInfo> showRoomImageInfoList = showRoomImageInfoRepository.findAllByShowRoomId(showRoomId);
        List<String> showRoomImageUrls = new ArrayList<>();
        for (ShowRoomImageInfo imageInfo : showRoomImageInfoList) {
            showRoomImageUrls.add(imageInfo.getFileUrl());
        }

        // 태그 가져오기
        List<Integer> hashTagIdList = showRoomHashTagRepository.findByShowRoomId(showRoomId);
        List<HashTag> hashTagList = new ArrayList<>();
        for (Integer hashTagId : hashTagIdList) {
            hashTagList.add(hashTagRepository.findById(hashTagId).get());
        }

        ShowRoomDetailResponseDto showRoomDetailResponseDto = new ShowRoomDetailResponseDto(showRoom.get(), hashTagList, showRoomImageUrls);

        // 로그인 체크
        if (memberId.toString().isEmpty()) { // 로그인 X
            return showRoomDetailResponseDto.addCheckLike(false);
        } else { // 로그인 O
            // 회원 체크
            verifyMember(memberId);

            LikeShowRoomId likeShowRoomId = new LikeShowRoomId(memberId, showRoomId);

            // 좋아요 목록 가져옴
            boolean checkLike = likeShowRoomRepository.existsById(likeShowRoomId);
            if (checkLike) {
                return showRoomDetailResponseDto.addCheckLike(true);
            } else {
                return showRoomDetailResponseDto.addCheckLike(false);
            }
        }
    }

    /**
     * 검색어 목록 가져오기
     *
     * @param searchRelatedListRequestDto
     * @return
     */
    public List<SearchRelatedListUniteResponseDto> getSearchRelatedListFinal(SearchRelatedListRequestDto
                                                                                     searchRelatedListRequestDto) {
        List<SearchRelatedListResponseDto> searchRelatedListResponseDtoList = getSearchRelatedList(searchRelatedListRequestDto);

        List<SearchRelatedListUniteResponseDto> searchRelatedListUniteResponseDtoList = new ArrayList<>();
        for (SearchRelatedListResponseDto s : searchRelatedListResponseDtoList) {
            /* 주소 매핑 */
            if (s.getAddress() != null) {
                searchRelatedListUniteResponseDtoList.add(new SearchRelatedListUniteResponseDto(s.getAddress(), "address", "", ""));
                continue;
            }

            /* 역 매핑 */
            if (s.getStation() != null) {
                searchRelatedListUniteResponseDtoList.add(new SearchRelatedListUniteResponseDto(s.getStation(), "station", s.getLocation().getLat(), s.getLocation().getLon()));
                continue;
            }

            /* 대학교 매핑 */
            searchRelatedListUniteResponseDtoList.add(new SearchRelatedListUniteResponseDto(s.getUniv(), "univ", s.getLocation().getLat(), s.getLocation().getLon()));
        }

        return searchRelatedListUniteResponseDtoList;
    }

    /**
     * 주소, 역, 대학교 목록 가져오기
     *
     * @param searchRelatedListRequestDto
     * @return
     */
    private List<SearchRelatedListResponseDto> getSearchRelatedList(SearchRelatedListRequestDto
                                                                            searchRelatedListRequestDto) {
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
    private boolean checkGuGunDongEupMyeonLi(String word) {
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
    public List<ShowRoomListResponseDto> getSearchResult(ShowRoomSearchRequestDto showRoomSearchRequestDto) throws
            BaseException {
        String searchType = showRoomSearchRequestDto.getSearchType();
        UUID memberId = showRoomSearchRequestDto.getMemberId();

        List<ShowRoomSearchResponseDto> showRoomSearchResponseDtoList;
        if (searchType.equals("total")) { // 전체 검색
            showRoomSearchResponseDtoList = getSearchTotalResult(showRoomSearchRequestDto);
        } else { // 부분 검색
            showRoomSearchResponseDtoList = getSearchNotTotalResult(showRoomSearchRequestDto);
        }

        List<ShowRoomListResponseDto> showRoomListResponseDtoList = getShowRoomList(showRoomSearchResponseDtoList);

        return checkLikeShowRoom(memberId, showRoomListResponseDtoList);
    }

    /**
     * 검색 결과로 나온 showRoomId로 showRoom 가져오기
     */
    private List<ShowRoomListResponseDto> getShowRoomList
    (List<ShowRoomSearchResponseDto> showRoomSearchResponseDtoList) {
        List<ShowRoomListResponseDto> showRoomListResponseDtoList = new ArrayList<>();

        for (ShowRoomSearchResponseDto cur : showRoomSearchResponseDtoList) {
            Integer showRoomId = cur.getShowRoomId(); // 검색 결과로 나온 곰방봐 id

            Optional<ShowRoom> showRoomOptional = showRoomRepository.findById(showRoomId); // 곰방봐 찾기
            ShowRoom showRoom = showRoomOptional.get();

            ShowRoomListResponseDto showRoomListResponseDto = new ShowRoomListResponseDto(showRoom, false);
            showRoomListResponseDtoList.add(showRoomListResponseDto);
        }

        return showRoomListResponseDtoList;
    }

    /**
     * 좋아요 체크
     */
    private List<ShowRoomListResponseDto> checkLikeShowRoom(UUID
                                                                    memberId, List<ShowRoomListResponseDto> showRoomListResponseDtoList) throws BaseException {
        /* 로그인 체크 */
        if (memberId.toString().isEmpty()) { // 로그인 X
            return showRoomListResponseDtoList;
        } else { // 로그인 O
            // 회원 체크
            verifyMember(memberId);

            // 좋아요 목록 가져옴
            for (ShowRoomListResponseDto showRoomListResponseDto : showRoomListResponseDtoList) {
                ShowRoom showRoom = showRoomListResponseDto.getShowRoom();
                Integer showRoomId = showRoom.getId();

                LikeShowRoomId likeShowRoomId = new LikeShowRoomId(memberId, showRoomId);

                boolean checkLike = likeShowRoomRepository.existsById(likeShowRoomId);
                if (checkLike) {
                    showRoomListResponseDto.setCheckLike(true);
                }
            }

            return showRoomListResponseDtoList;
        }
    }

    /**
     * 해시태그 Query 생성
     *
     * @param hashTag
     * @return
     */
    private ArrayList<QueryBuilder> makeMatchQuery(String hashTag) {
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
    private ArrayList<ShowRoomSearchResponseDto> makeSearchQuery(BoolQueryBuilder boolQueryBuilder,
                                                                 int pageOffset, String sortType) {
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
    private List<ShowRoomSearchResponseDto> getSearchTotalResult(ShowRoomSearchRequestDto showRoomSearchRequestDto) {
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
    private List<ShowRoomSearchResponseDto> getSearchNotTotalResult(ShowRoomSearchRequestDto
                                                                            showRoomSearchRequestDto) {
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

    @Transactional
    public void saveImages(List<String> fileUrls, ShowRoomResponseDto showRoomResponseDto) {
        showRoomResponseDto.addFileUrls(fileUrls);

        showRoomResponseDto.getShowRoom().saveThumbnail(fileUrls.get(0));
    }
}
