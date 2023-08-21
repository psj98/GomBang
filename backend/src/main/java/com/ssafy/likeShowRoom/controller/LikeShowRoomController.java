package com.ssafy.likeShowRoom.controller;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.likeShowRoom.dto.LikeShowRoomDeleteRequestDto;
import com.ssafy.likeShowRoom.dto.LikeShowRoomMyListRequestDto;
import com.ssafy.likeShowRoom.dto.LikeShowRoomRegisterRequestDto;
import com.ssafy.likeShowRoom.service.LikeShowRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeShowRoomController {

    private final ResponseService responseService;
    private final LikeShowRoomService likeShowRoomService;

    /**
     * 좋아요 등록
     */
    @PostMapping("/register")
    public BaseResponse<Object> registerLikeShowRoom(@RequestBody LikeShowRoomRegisterRequestDto likeShowRoomRegisterRequestDto) {
        try {
            return responseService.getSuccessResponse(likeShowRoomService.registerLikeShowRoom(likeShowRoomRegisterRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 좋아요 해제
     */
    @DeleteMapping("/delete")
    public BaseResponse<Object> deleteLikeShowRoom(@RequestBody LikeShowRoomDeleteRequestDto likeShowRoomDeleteRequestDto) {
        try {
            return responseService.getSuccessResponse(likeShowRoomService.deleteLikeShowRoom(likeShowRoomDeleteRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 내가 좋아요한 목록
     */
    @PostMapping("/my-list")
    public BaseResponse<Object> getMyLikeShowRoomList(@RequestBody LikeShowRoomMyListRequestDto likeShowRoomMyListRequestDto) {
        try {
            return responseService.getSuccessResponse(likeShowRoomService.getMyLikeShowRoomList(likeShowRoomMyListRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }
}
