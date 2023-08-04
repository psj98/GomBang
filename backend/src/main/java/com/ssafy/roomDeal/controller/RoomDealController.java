package com.ssafy.roomDeal.controller;

import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.roomDeal.dto.RoomDealRegisterRequestDto;
import com.ssafy.roomDeal.dto.SearchNearestStationUnivRequestDto;
import com.ssafy.roomDeal.service.RoomDealService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomdeal")
public class RoomDealController {

    private final RoomDealService roomDealService;

    @PostMapping("/register")
    public BaseResponse<Object> registerRoomDeal(@RequestBody RoomDealRegisterRequestDto roomDealRegisterRequestDto) {
        return new BaseResponse<>();
    }

    @GetMapping("/hehe")
    public void hehe() {
        System.out.println("hehe");
    }

    @PostMapping("/search-station-univ")
    public BaseResponse<Object> searchNearestStationUniv(@RequestBody SearchNearestStationUnivRequestDto searchNearestStationUnivRequestDto) {

        roomDealService.searchNearestStationUniv(searchNearestStationUnivRequestDto);

        return null;
    }

    // 등록 하기 전에 주소 먼저 넘겨 받고 역, 역거리, 학교, 학교거리 넘겨주는 메서드 해야함
    // 위도 경도는 언제 받지? 아마 주소 넘겨받을 때 같이 넘겨받을 수 있을거 같은데
    // 사용자가 다른 역, 학교 입력했을때 글 등록할때 가까운걸로 그냥 넣을건지, 아니면 중간에 한번 체크해서 사용자한테 어떤 역으로 할건지 알려줄건지?


}
