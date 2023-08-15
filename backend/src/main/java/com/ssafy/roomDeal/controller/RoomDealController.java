package com.ssafy.roomDeal.controller;

import com.ssafy.elasticsearch.dto.*;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.member.domain.Member;
import com.ssafy.notification.domain.NotificationType;
import com.ssafy.notification.service.NotificationService;
import com.ssafy.roomDeal.dto.*;
import com.ssafy.roomDeal.service.RoomDealService;
import com.ssafy.s3.service.S3Service;
import com.ssafy.starRoomDeal.domain.StarMemberMapping;
import com.ssafy.starRoomDeal.dto.StarRoomDealMemberListResponseDto;
import com.ssafy.starRoomDeal.service.StarRoomDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomdeal")
public class RoomDealController {

    private final ResponseService responseService;
    private final RoomDealService roomDealService;
    private final S3Service s3Service;
    private final StarRoomDealService starRoomDealService;
    private final NotificationService notificationService;

    /**
     * 매물 등록
     * @param roomDealRegisterRequestDto
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Object> registerRoomDeal(@RequestPart(name="files")List<MultipartFile> files, @RequestPart RoomDealRegisterRequestDto roomDealRegisterRequestDto) {
        RoomDealResponseDto roomDealResponseDto = roomDealService.registerRoomDeal(roomDealRegisterRequestDto);
        List<String> fileUrls;
        try {
            fileUrls = s3Service.uploadFiles(files, roomDealResponseDto.getRoomDeal().getId());
            roomDealService.saveImages(fileUrls, roomDealResponseDto);
        } catch (BaseException e) {
            responseService.getFailureResponse(e.status);
        }
        return responseService.getSuccessResponse(roomDealResponseDto);
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
            // 매물 수정
            RoomDealResponseDto roomDealResponseDto = roomDealService.updateRoomDeal(roomDealUpdateRequestDto);

            // 해당 매물을 찜 한 사용자 리스트 조회
            StarRoomDealMemberListResponseDto starRoomDealMemberListResponseDto = starRoomDealService.getRoomDealStarredMemberList(roomDealResponseDto.getRoomDeal().getId());

            // 찜 한 사용자들에게 매물 가격 변동 알림 전송
            for (StarMemberMapping starMemberMapping : starRoomDealMemberListResponseDto.getStarMemberList()) {
                Member member = starMemberMapping.getMember(); // StarMemberMapping을 Member로 변환
                notificationService.send(member, NotificationType.LIKED, member.getName() + "님이 찜 한 매물의 가격이 변동되었습니다.", "/roomdeal/" + roomDealResponseDto.getRoomDeal().getId());
            }

            return responseService.getSuccessResponse(roomDealResponseDto);
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

        List<RoomDealListResponseDto> roomDealListResponseDtoList;
        try {
            roomDealListResponseDtoList = roomDealService.getRoomDealByIdAtCache(roomDealSearchResponseDtoList, searchByAddressRequestDto.getAddress());
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }

        return responseService.getSuccessResponse(roomDealListResponseDtoList);
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
     * @param locationRequestDto
     * @return
     */
    @PostMapping("/search-nearest")
    public BaseResponse<Object> searchNearestStationUniv(@RequestBody LocationRequestDto locationRequestDto){
        RoomDealNearestStationUnivResponseDto roomDealNearestStationResponseDto = roomDealService.getNearestStationUniv(locationRequestDto);
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

    @PostMapping("/filter")
    public BaseResponse<Object> getFilteredRoomDealList(@RequestBody FilteredRoomDealListRequestDto filteredRoomDealListRequestDto) {
        List<RoomDealListResponseDto> roomDealListResponseDtoList;
        try {
            roomDealListResponseDtoList = roomDealService.filterRoomDeal(filteredRoomDealListRequestDto);
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }

        return responseService.getSuccessResponse(roomDealListResponseDtoList);
    }

}
