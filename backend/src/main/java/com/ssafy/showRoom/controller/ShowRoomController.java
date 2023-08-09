package com.ssafy.showRoom.controller;

import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.showRoom.dto.ShowRoomRegisterRequestDto;
import com.ssafy.showRoom.service.ShowRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/showroom")
@RequiredArgsConstructor
public class ShowRoomController {

    private final ResponseService responseService;

    private final ShowRoomService showRoomService;

    /**
     * 곰방봐 등록
     * @param showRoomRegisterRequestDto
     * @return BaseResponse<ShowRoomResponseDto>
     */
    @PostMapping("/register")
    public BaseResponse<Object> registerShowRoom(@RequestBody ShowRoomRegisterRequestDto showRoomRegisterRequestDto) {
        System.out.println(showRoomRegisterRequestDto.getRoomDealId());
        System.out.println(showRoomRegisterRequestDto.getMemberId().toString());
        try {
            return responseService.getSuccessResponse("곰방봐 등록 성공", showRoomService.registerShowRoom(showRoomRegisterRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }
}
