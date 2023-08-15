package com.ssafy.starRoomDeal.controller;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.starRoomDeal.dto.StarRoomDealDeleteRequestDto;
import com.ssafy.starRoomDeal.dto.StarRoomDealRegisterRequestDto;
import com.ssafy.starRoomDeal.service.StarRoomDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/star")
public class StarRoomDealController {

    private final ResponseService responseService;
    private final StarRoomDealService starRoomDealService;

    /**
     * 매물 글을 찜한다.
     *
     * @param starRoomDealRegisterRequestDto 찜 할 사용자의 UUID, 찜 할 매물 글의 id
     * @return 사용자 정보, 매물 글 정보
     */
    @PostMapping("/register")
    public BaseResponse<Object> registerStarRoomDeal(@RequestBody StarRoomDealRegisterRequestDto starRoomDealRegisterRequestDto) {
        try {
            return responseService.getSuccessResponse(starRoomDealService.registerStarRoomDeal(starRoomDealRegisterRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 매물 글에 등록한 찜을 취소한다.
     *
     * @param starRoomDealDeleteRequestDto 찜 할 사용자의 UUID, 찜 할 매물 글의 id
     * @return 사용자의 UUID, 매물 글의 id
     */
    @DeleteMapping("/delete")
    public BaseResponse<Object> deleteStarRoomDeal(@RequestBody StarRoomDealDeleteRequestDto starRoomDealDeleteRequestDto) {
        try {
            return responseService.getSuccessResponse(starRoomDealService.deleteStarRoomDeal(starRoomDealDeleteRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 사용자가 찜한 매물 글 목록을 조회한다.
     *
     * @param memberId 사용자의 UUID
     * @return 사용자가 찜한 매물 글 리스트
     */
    @GetMapping("/my-list/{memberId}")
    public BaseResponse<Object> getMyStarRoomDealList(@PathVariable UUID memberId) {
        try {
            return responseService.getSuccessResponse(starRoomDealService.getMyStarRoomDealList(memberId));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 매물을 찜한 사용자 목록을 조회한다.
     *
     * @param roomDealId 매물 글의 id
     * @return 매물을 찜한 사용자 리스트
     */
    @GetMapping("/member-list/{roomDealId}")
    public BaseResponse<Object> getRoomDealStarredMemberList(@PathVariable Long roomDealId) {
        try {
            return responseService.getSuccessResponse(starRoomDealService.getRoomDealStarredMemberList(roomDealId));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

}
