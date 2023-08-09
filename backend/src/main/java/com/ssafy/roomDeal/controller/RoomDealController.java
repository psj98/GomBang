package com.ssafy.roomDeal.controller;

import com.ssafy.elasticsearch.dto.RoomDealNearestStationDto;
import com.ssafy.elasticsearch.dto.RoomDealSearchDto;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.roomDeal.dto.RoomDealDeleteRequestDto;
import com.ssafy.roomDeal.dto.RoomDealRegisterRequestDto;
import com.ssafy.roomDeal.dto.RoomDealUpdateRequestDto;
import com.ssafy.elasticsearch.dto.SearchByAddressRequestDto;
import com.ssafy.elasticsearch.dto.SearchNearestStationUnivRequestDto;
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
     * @param searchByAddressRequestDto
     * @return
     */
    @GetMapping("/search-address")
    @Cacheable(value="address", key = "#searchByAddressRequestDto.address", cacheManager = "cacheManager")
    public BaseResponse<Object> searchByAddress(@RequestBody SearchByAddressRequestDto searchByAddressRequestDto) {
        List<RoomDealSearchDto> roomDealSearchDtos = roomDealService.searchByAddress(searchByAddressRequestDto);
        return responseService.getSuccessResponse(roomDealSearchDtos);
    }

    /**
     * 역, 학교로 매물 검색 + 본문 검색
     * @param searchNearestStationUnivRequestDto
     * @return
     */
    @GetMapping("/search-station-univ")
    public BaseResponse<Object> searchNearestStationUniv(@RequestBody SearchNearestStationUnivRequestDto searchNearestStationUnivRequestDto) {
        List<RoomDealSearchDto> roomDealSearchDtos = roomDealService.searchByLocation(searchNearestStationUnivRequestDto);
        return responseService.getSuccessResponse(roomDealSearchDtos);
    }

    /**
     * 본문 검색
     * @param content
     * @return
     */
    @GetMapping("/search-content")
    public BaseResponse<Object> searchByContent(@RequestBody String content){
        List<RoomDealSearchDto> roomDealSearchDtos = roomDealService.searchByContent(content);
        return responseService.getSuccessResponse(roomDealSearchDtos);
    }

    /**
     * 주소 위도, 경도 기반으로 가까운 역 검색
     * @param searchNearestStationUnivRequestDto
     * @return
     */
    @GetMapping("/search-nearest-station")
    public BaseResponse<Object> searchNearestStation(@RequestBody SearchNearestStationUnivRequestDto searchNearestStationUnivRequestDto){
        List<RoomDealNearestStationDto> roomDealNearestStationDtos = roomDealService.getNearestStation(searchNearestStationUnivRequestDto);
        return responseService.getSuccessResponse(roomDealNearestStationDtos);
    }

}
