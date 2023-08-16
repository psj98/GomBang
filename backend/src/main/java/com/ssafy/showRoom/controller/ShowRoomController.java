package com.ssafy.showRoom.controller;

import com.ssafy.elasticsearch.dto.SearchRelatedListRequestDto;
import com.ssafy.elasticsearch.dto.SearchRelatedListUniteResponseDto;
import com.ssafy.elasticsearch.dto.ShowRoomSearchRequestDto;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.showRoom.dto.ShowRoomDeleteRequestDto;
import com.ssafy.showRoom.service.ShowRoomService;
import com.ssafy.showRoomHashTag.dto.ShowRoomHashTagRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showroom")
@RequiredArgsConstructor
public class ShowRoomController {

    private final ResponseService responseService;

    private final ShowRoomService showRoomService;

    /**
     * 곰방봐 등록
     *
     * @param showRoomHashTagRequestDto
     * @return BaseResponse<ShowRoomResponseDto>
     */
    @PostMapping("/register")
    public BaseResponse<Object> registerShowRoom(@RequestBody ShowRoomHashTagRequestDto showRoomHashTagRequestDto) {
        try {
            return responseService.getSuccessResponse(showRoomService.registerShowRoom(showRoomHashTagRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 곰방봐 상세보기
     * 
     * @param showRoomDeleteRequestDto
     * @return
     */
    @PostMapping("/detail")
    public BaseResponse<Object> getShowRoom(@RequestBody ShowRoomDeleteRequestDto showRoomDeleteRequestDto) {
        try {
            return responseService.getSuccessResponse(showRoomService.getShowRoom(showRoomDeleteRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }

    /**
     * 곰방봐 삭제
     *
     * @param showRoomDeleteRequestDto
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResponse<Object> deleteShowRoom(@RequestBody ShowRoomDeleteRequestDto showRoomDeleteRequestDto) {
        try {
            return responseService.getSuccessResponse(showRoomService.deleteShowRoom(showRoomDeleteRequestDto));
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
    public BaseResponse<Object> getRelatedList(@RequestBody SearchRelatedListRequestDto searchRelatedListRequestDto) {
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
        try {
            return responseService.getSuccessResponse(showRoomService.getSearchResult(showRoomSearchRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }
    }
}
