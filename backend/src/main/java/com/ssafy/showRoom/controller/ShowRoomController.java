package com.ssafy.showRoom.controller;

import com.ssafy.elasticsearch.dto.SearchRelatedListRequestDto;
import com.ssafy.elasticsearch.dto.SearchRelatedListUniteResponseDto;
import com.ssafy.elasticsearch.dto.ShowRoomSearchRequestDto;
import com.ssafy.elasticsearch.dto.ShowRoomSearchResponseDto;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.showRoom.dto.ShowRoomRegisterRequestDto;
import com.ssafy.showRoom.dto.ShowRoomResponseDto;
import com.ssafy.showRoom.service.ShowRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            return responseService.getSuccessResponse(showRoomService.registerShowRoom(showRoomRegisterRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 검색어 목록 가져오기
     *
     * @param searchRelatedListRequestDto
     * @return
     */
    @PostMapping("/search-related-list")
    public BaseResponse<Object> getRelatedList(@RequestBody SearchRelatedListRequestDto searchRelatedListRequestDto){
        List<SearchRelatedListUniteResponseDto> searchRelatedListUniteResponseDtoList = showRoomService.getSearchRelatedListFinal(searchRelatedListRequestDto);
        return responseService.getSuccessResponse(searchRelatedListUniteResponseDtoList);
    }

    /**
     * 곰방봐 검색 (검색어 + 해시태그 + 정렬)
     *
     * @param showRoomSearchRequestDto
     * @return
     */
    @PostMapping("/search-result")
    public BaseResponse<Object> getSearchResult(@RequestBody ShowRoomSearchRequestDto showRoomSearchRequestDto) {
        List<ShowRoomSearchResponseDto> showRoomSearchResponseDtoList = showRoomService.getSearchResult(showRoomSearchRequestDto);
        return responseService.getSuccessResponse(showRoomSearchResponseDtoList);
    }
}
