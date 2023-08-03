package com.ssafy.member.controller;


import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.member.dto.KakaoTokenResponseDto;
import com.ssafy.member.dto.MemberUpdateRequestDto;
import com.ssafy.member.dto.KakaoLoginResponseDto;
import com.ssafy.member.dto.MemberLoginResponseDto;
import com.ssafy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final ResponseService responseService;
    private final MemberService memberService;

    // 카카오 로그인 요청 처리
    @GetMapping("/login")
    public BaseResponse<Object> loginCallBack(HttpServletRequest request) {
        // 인가코드로 토큰 발급
        KakaoTokenResponseDto kakaoToken = memberService.getKaKaoToken(request.getParameter("code"));
        // 토큰으로 카카오 사용자 정보 요청 (channelId, email, gender)
        KakaoLoginResponseDto kakaoLoginResponseDto = memberService.getMemberInfoWithToken(kakaoToken);

        // 사용자 channelId로 회원가입 여부 확인
        try {
            MemberLoginResponseDto loginMember = new MemberLoginResponseDto(memberService.getMemberInfoWithChannelId(kakaoLoginResponseDto.getId()));

            System.out.println(loginMember.toString());
            return responseService.getSuccessResponse("로그인 성공", loginMember);
        } catch (NoSuchElementException e) {
            MemberLoginResponseDto joinMember = new MemberLoginResponseDto(memberService.join(kakaoLoginResponseDto));
            return responseService.getSuccessResponse("회원가입 성공", joinMember);
        }
    }

    // 사용자 이름 수정 요청 처리
    @PostMapping("/update")
    public BaseResponse<Object> updateName(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        return responseService.getSuccessResponse("이름 수정 성공", memberService.updateMemberName(memberUpdateRequestDto));
    }
}
