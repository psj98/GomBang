package com.ssafy.member.controller;


import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.member.dto.*;
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

    /**
     * 카카오 로그인 요청 처리
     * request_url=https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=a20ef37212e1ae86b20e09630f6590ce&redirect_uri=http://localhost:8080/member/login
     * @param request
     * @return
     */
    @GetMapping("/login")
    public BaseResponse<Object> loginCallBack(HttpServletRequest request) {
        // 인가코드로 토큰 발급
        KakaoTokenResponseDto kakaoToken = memberService.getKaKaoToken(request.getParameter("code"));
        // 토큰으로 카카오 사용자 정보 요청 (channelId, email, gender)
        KakaoLoginResponseDto kakaoLoginResponseDto = memberService.getMemberInfoWithToken(kakaoToken);
        MemberLoginResponseDto memberLoginResponseDto = null;
        // 사용자 channelId로 회원가입 여부 확인
        try {
            // DB에 존재하는 사용자면 로그인
            memberLoginResponseDto = new MemberLoginResponseDto(memberService.getMemberInfoWithChannelId(kakaoLoginResponseDto.getId()));
            System.out.println(memberLoginResponseDto.toString());
            // 회원가입은 됐지만 이름 입력이 안된 사용자면 UUID와 함께 이름 입력 화면으로 반환
            if (memberLoginResponseDto.getMember().getName().equals("사용자"))
                throw new IllegalAccessException();
            return responseService.getSuccessResponse("로그인 성공", memberLoginResponseDto);
        } catch (NoSuchElementException e) {
            // DB에 존재하지 않는 사용자면 회원가입
            MemberJoinResponseDto memberJoinResponseDto = new MemberJoinResponseDto(memberService.join(kakaoLoginResponseDto).getId());
            System.out.println(memberJoinResponseDto.toString());
            return responseService.getSuccessResponse("회원가입 성공", memberJoinResponseDto);
        } catch (IllegalAccessException e) {
            // DB에 가입했지만 이름을 입력하지 않은 사용자면 UUID만 반환해서 이름 입력 창으로 전환
            MemberJoinResponseDto memberJoinResponseDto = new MemberJoinResponseDto(memberLoginResponseDto.getMember().getId());
            System.out.println(memberJoinResponseDto.toString());
            return responseService.getSuccessResponse("이름 입력 안된 사용자", memberJoinResponseDto);
        }
    }

    /**
     * 사용자 이름 수정 요청 처리
     * @param memberUpdateRequestDto
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Object> updateName(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        MemberLoginResponseDto memberLoginResponseDto = new MemberLoginResponseDto(memberService.updateMemberName(memberUpdateRequestDto));
        System.out.println(memberLoginResponseDto);
        return responseService.getSuccessResponse("이름 수정 성공", memberLoginResponseDto);
    }
}
