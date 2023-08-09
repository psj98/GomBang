package com.ssafy.roomDeal.controller;

import com.ssafy.elasticsearch.dto.*;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.roomDeal.dto.RoomDealDeleteRequestDto;
import com.ssafy.roomDeal.dto.RoomDealRegisterRequestDto;
import com.ssafy.roomDeal.dto.RoomDealUpdateRequestDto;
import com.ssafy.roomDeal.service.RoomDealService;
import lombok.RequiredArgsConstructor;
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
    public BaseResponse<Object> getRoomDeal(@PathVariable("id") Long id) {
        try {
            return responseService.getSuccessResponse(roomDealService.getRoomDeal(id));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

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
     * @param searchByAddressRequestDto
     * @return
     */
    @GetMapping("/search-address")
    public BaseResponse<Object> searchByAddress(@RequestBody SearchByAddressRequestDto searchByAddressRequestDto) {
        List<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = roomDealService.searchByAddress(searchByAddressRequestDto);
        return responseService.getSuccessResponse(roomDealSearchResponseDtoList);
    }

    /**
     * 역, 학교로 매물 검색 + 본문 검색
     * @param searchByStationUnivRequestDto
     * @return
     */
    @GetMapping("/search-station-univ")
    public BaseResponse<Object> searchNearestStationUniv(@RequestBody SearchByStationUnivRequestDto searchByStationUnivRequestDto) {
        List<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = roomDealService.searchByLocation(searchByStationUnivRequestDto);
        return responseService.getSuccessResponse(roomDealSearchResponseDtoList);
    }

    /**
     * 본문 검색
     * @param content
     * @return
     */
    @GetMapping("/search-content")
    public BaseResponse<Object> searchByContent(@RequestBody String content){
        List<RoomDealSearchResponseDto> roomDealSearchResponseDtoList = roomDealService.searchByContent(content);
        return responseService.getSuccessResponse(roomDealSearchResponseDtoList);
    }

    /**
     * 주소 위도, 경도 기반으로 가까운 역 검색
     * @param searchByStationUnivRequestDto
     * @return
     */
    @GetMapping("/search-nearest-station")
    public BaseResponse<Object> searchNearestStation(@RequestBody SearchByStationUnivRequestDto searchByStationUnivRequestDto){
        List<RoomDealNearestStationDto> roomDealNearestStationDtoList = roomDealService.getNearestStation(searchByStationUnivRequestDto);
        return responseService.getSuccessResponse(roomDealNearestStationDtoList);
    }

    /**
     * 주소 목록 가져오기
     * @param addressSearchListRequestDto
     * @return
     */
    @GetMapping("/address-list")
    public BaseResponse<Object> getAddressList(@RequestBody AddressSearchListRequestDto addressSearchListRequestDto){
        List<AddressSearchListResponseDto> addressSearchListResponseDtoList = roomDealService.getAddressList(addressSearchListRequestDto);
        return responseService.getSuccessResponse("주소 목록 리스트 가져오기 성공", addressSearchListResponseDtoList);
    }
}
