package com.ssafy.roomDeal.controller;

import com.ssafy.elasticsearch.dto.RoomDealNearestStationDto;
import com.ssafy.elasticsearch.dto.RoomDealSearchDto;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.roomDeal.dto.RoomDealRegisterRequestDto;
import com.ssafy.roomDeal.dto.RoomDealUpdateRequestDto;
import com.ssafy.elasticsearch.dto.SearchByAddressRequestDto;
import com.ssafy.elasticsearch.dto.SearchNearestStationUnivRequestDto;
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

    // 매물 등록
    @PostMapping("/register")
    public BaseResponse<Object> registerRoomDeal(@RequestBody RoomDealRegisterRequestDto roomDealRegisterRequestDto) {
        return responseService.getSuccessResponse("매물 등록 성공", roomDealService.registerRoomDeal(roomDealRegisterRequestDto));
    }

    // 매물 조회
    @GetMapping("/{id}")
    public BaseResponse<Object> getRoomDeal(@PathVariable("id") Long id) {
        try {
            return responseService.getSuccessResponse("매물 조회 성공", roomDealService.getRoomDeal(id));
        } catch (IllegalArgumentException e) {
            return responseService.getFailureResponse(e.getMessage());
        }
    }

    // 매물 수정
    @PutMapping("/update")
    public BaseResponse<Object> updateRoomDeal(@RequestBody RoomDealUpdateRequestDto roomDealUpdateRequestDto) {
        return responseService.getSuccessResponse("매물 수정 성공", roomDealService.updateRoomDeal(roomDealUpdateRequestDto));
    }

    // 매물 삭제
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Object> deledeRoomDeal(@PathVariable("id") Long id) {
        return responseService.getSuccessResponse("매물 삭제 성공", roomDealService.deleteRoomDeal(id));
    }

    /**
     * 주소로 매물 검색 + 본문 검색
     * @param searchByAddressRequestDto
     * @return
     */
    @PostMapping("/search-address")
    public BaseResponse<Object> searchByAddress(@RequestBody SearchByAddressRequestDto searchByAddressRequestDto) {
        List<RoomDealSearchDto> roomDealSearchDtos = roomDealService.searchByAddress(searchByAddressRequestDto);
        return responseService.getSuccessResponse("주소 매물 검색 성공", roomDealSearchDtos);
    }

    /**
     * 역, 학교로 매물 검색 + 본문 검색
     * @param searchNearestStationUnivRequestDto
     * @return
     */
    @PostMapping("/search-station-univ")
    public BaseResponse<Object> searchNearestStationUniv(@RequestBody SearchNearestStationUnivRequestDto searchNearestStationUnivRequestDto) {
        List<RoomDealSearchDto> roomDealSearchDtos = roomDealService.searchByLocation(searchNearestStationUnivRequestDto);
        return responseService.getSuccessResponse("주소 매물 검색 성공", roomDealSearchDtos);
    }

    /**
     * 본문 검색
     * @param content
     * @return
     */
    @GetMapping("/search-content")
    public BaseResponse<Object> searchByContent(@RequestBody String content){
        List<RoomDealSearchDto> roomDealSearchDtos = roomDealService.searchByContent(content);
        return responseService.getSuccessResponse("본문 검색 성공", roomDealSearchDtos);
    }

    /**
     * 주소 위도, 경도 기반으로 가까운 역 검색
     * @param searchNearestStationUnivRequestDto
     * @return
     */
    @GetMapping("/search-nearest-station")
    public BaseResponse<Object> searchNearestStation(@RequestBody SearchNearestStationUnivRequestDto searchNearestStationUnivRequestDto){
        List<RoomDealNearestStationDto> roomDealNearestStationDtos = roomDealService.getNearestStation(searchNearestStationUnivRequestDto);
        return responseService.getSuccessResponse("가까운 역 가져오기 성공", roomDealNearestStationDtos);
    }

    // 등록 하기 전에 주소 먼저 넘겨 받고 역, 역거리, 학교, 학교거리 넘겨주는 메서드 해야함
    // 위도 경도는 언제 받지? 아마 주소 넘겨받을 때 같이 넘겨받을 수 있을거 같은데
    // 사용자가 다른 역, 학교 입력했을때 글 등록할때 가까운걸로 그냥 넣을건지, 아니면 중간에 한번 체크해서 사용자한테 어떤 역으로 할건지 알려줄건지?
}
