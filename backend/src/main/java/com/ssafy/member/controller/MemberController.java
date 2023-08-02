package com.ssafy.member.controller;


import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.member.dto.MemberJoinRequestDto;
import com.ssafy.member.dto.MemberLoginRequestDto;
import com.ssafy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final ResponseService responseService;
    private final MemberService memberService;

    // 카카오 로그인 요청 처리
    @GetMapping("/login")
    public BaseResponse<Object> loginCallBack(HttpServletRequest request) {
        MemberLoginRequestDto kakaoInfo = memberService.getKaKaoInfo(request.getParameter("code"));

        return;
    }

    @PostMapping("/join")
    public BaseResponse<Object> join(@RequestBody MemberJoinRequestDto memberJoinRequestDto) {
        return responseService.getSuccessResponse("회원가입 성공", memberService.join(memberJoinRequestDto));
    }
}
