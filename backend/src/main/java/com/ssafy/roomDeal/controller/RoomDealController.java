package com.ssafy.roomDeal.controller;

import com.ssafy.elasticsearch.dto.RoomDealSearchDto;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.roomDeal.dto.RoomDealRegisterRequestDto;
import com.ssafy.roomDeal.dto.RoomDealUpdateRequestDto;
import com.ssafy.roomDeal.dto.SearchByAddressRequestDto;
import com.ssafy.roomDeal.dto.SearchNearestStationUnivRequestDto;
import com.ssafy.roomDeal.service.RoomDealService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
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

    // 주소로 매물 찾기
    @PostMapping("/search-address")
    public BaseResponse<Object> searchByAddress(@RequestBody SearchByAddressRequestDto searchByAddressRequestDto) {

        List<RoomDealSearchDto> roomDealSearchDtos = roomDealService.searchByAddress(searchByAddressRequestDto);

        for (RoomDealSearchDto r : roomDealSearchDtos) {
            System.out.println(r.toString());
        }

        return null;
    }

    // 주변 역, 학교 찾기
    @PostMapping("/search-station-univ")
    public BaseResponse<Object> searchNearestStationUniv(@RequestBody SearchNearestStationUnivRequestDto searchNearestStationUnivRequestDto) {

        List<RoomDealSearchDto> roomDealSearchDtos = roomDealService.searchByLocation(searchNearestStationUnivRequestDto);

        for (RoomDealSearchDto r : roomDealSearchDtos) {
            System.out.println(r.toString());
        }

        return null;
    }

    // 등록 하기 전에 주소 먼저 넘겨 받고 역, 역거리, 학교, 학교거리 넘겨주는 메서드 해야함
    // 위도 경도는 언제 받지? 아마 주소 넘겨받을 때 같이 넘겨받을 수 있을거 같은데
    // 사용자가 다른 역, 학교 입력했을때 글 등록할때 가까운걸로 그냥 넣을건지, 아니면 중간에 한번 체크해서 사용자한테 어떤 역으로 할건지 알려줄건지?


}
