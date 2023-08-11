package com.ssafy.roomDeal.controller;

import com.ssafy.elasticsearch.dto.*;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.roomDeal.domain.RoomDeal;
import com.ssafy.roomDeal.dto.RoomDealDeleteRequestDto;
import com.ssafy.roomDeal.dto.RoomDealRegisterRequestDto;
import com.ssafy.roomDeal.dto.RoomDealUpdateRequestDto;
import com.ssafy.roomDeal.service.RoomDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomdeal")
public class RoomDealController {

    private final ResponseService responseService;
    private final RoomDealService roomDealService;

    /**
     * 매물 등록
     * @param roomDealRegisterRequestDto
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Object> registerRoomDeal(@RequestBody RoomDealRegisterRequestDto roomDealRegisterRequestDto) {
        return responseService.getSuccessResponse(roomDealService.registerRoomDeal(roomDealRegisterRequestDto));
    }

    /**
     * 매물 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Cacheable(value="RoomDeal")
    public BaseResponse<Object> getRoomDeal(@PathVariable("id") Long id) {
        try {
            return responseService.getSuccessResponse(roomDealService.getRoomDeal(id));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }
//    여기다가 캐시어블 할게 아니라 메인검색에서 캐싱하자, 그리고 키는 검색어로 해야돼

    /**
     * 매물 수정
     * @param roomDealUpdateRequestDto
     * @return
     */
    @PutMapping("/update")
    public BaseResponse<Object> updateRoomDeal(@RequestBody RoomDealUpdateRequestDto roomDealUpdateRequestDto) {
        try {
            return responseService.getSuccessResponse(roomDealService.updateRoomDeal(roomDealUpdateRequestDto));
        } catch (BaseException e){
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 매물 삭제
     * @param roomDealDeleteRequestDto
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResponse<Object> deledeRoomDeal(@RequestBody RoomDealDeleteRequestDto roomDealDeleteRequestDto) {
        try {
            return responseService.getSuccessResponse(roomDealService.deleteRoomDeal(roomDealDeleteRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 주소로 매물 검색 + 본문 검색
     *
     * @param searchByAddressRequestDto
     * @return
     */
    @PostMapping("/search-address")
    public BaseResponse<Object> searchByAddress(@RequestBody SearchByAddressRequestDto searchByAddressRequestDto) {
        List<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = roomDealService.searchByAddress(searchByAddressRequestDto);

        List<RoomDeal> roomDeals;
        try {
            roomDeals = roomDealService.getRoomDealByIdAtCache(roomDealSearchResponseDtoList);
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }

        return responseService.getSuccessResponse(roomDeals);
    }

    /**
     * 역, 학교로 매물 검색 + 본문 검색
     *
     * @param searchByStationUnivRequestDto
     * @return
     */
    @PostMapping("/search-station-univ")
    public BaseResponse<Object> searchByStationUniv(@RequestBody SearchByStationUnivRequestDto searchByStationUnivRequestDto) {
        List<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = roomDealService.searchByLocation(searchByStationUnivRequestDto);
        return responseService.getSuccessResponse(roomDealSearchResponseDtoList);
    }

    /**
     * 본문 검색
     *
     * @param content
     * @return
     */
    @PostMapping("/search-content")
    public BaseResponse<Object> searchByContent(@RequestBody String content){
        List<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = roomDealService.searchByContent(content);
        return responseService.getSuccessResponse(roomDealSearchResponseDtoList);
    }

    /**
     * 주소 위도, 경도 기반으로 가까운 역, 대학교 검색
     *
     * @param searchByStationUnivRequestDto
     * @return
     */
    @PostMapping("/search-nearest")
    public BaseResponse<Object> searchNearestStationUniv(@RequestBody SearchByStationUnivRequestDto searchByStationUnivRequestDto){
        RoomDealNearestStationUnivResponseDto roomDealNearestStationResponseDto = roomDealService.getNearestStationUniv(searchByStationUnivRequestDto);
        return responseService.getSuccessResponse(roomDealNearestStationResponseDto);
    }

    /**
     * 검색어 목록 가져오기
     *
     * @param searchRelatedListRequestDto
     * @return
     */
    @PostMapping("/search-related-list")
    public BaseResponse<Object> getRelatedList(@RequestBody SearchRelatedListRequestDto searchRelatedListRequestDto){
        List<SearchRelatedListUniteResponseDto> searchRelatedListUniteResponseDtoList = roomDealService.getSearchRelatedListFinal(searchRelatedListRequestDto);
        return responseService.getSuccessResponse(searchRelatedListUniteResponseDtoList);
    }

}
