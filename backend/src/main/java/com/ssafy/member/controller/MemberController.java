package com.ssafy.member.controller;


import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponse;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.global.common.response.ResponseService;
import com.ssafy.member.dto.*;
import com.ssafy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

/**
 * @author 변지혜
 * @version 1.0
 * @see "com.ssafy.member.*"
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final ResponseService responseService;
    private final MemberService memberService;

    /**
     * 카카오 로그인 요청을 처리한다.
     *
     * request_url=https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=a20ef37212e1ae86b20e09630f6590ce&redirect_uri=http://localhost:3000/auth
     * @param kakaoLoginRequestDto 카카오 인가토큰을 입력받는다.
     * @return 로그인하는 경우 : LOGIN_SUCCESS와 회원 정보를 반환한다.
     * @return 회원가입하는 경우 : JOIN_SUCCESS와 새롭게 생성된 UUID를 반환한다.
     * @return 회원가입 후 사용자 이름을 입력하지 않은 경우 : JOINED_BUT_NO_NAME와 회원가입 시 생성된 UUID를 반환한다.
     * @return 카카오 인가 코드가 입력되지 않은 경우 : NOT_FOUND_KAKAO_CODE를 반환한다.
     * @return 카카오 API 호출에 실패한 경우 : KAKAO_API_CALL_FAILED를 반환한다.
     * @return 로그인에 실패한 경우 : LOGIN_FAILED를 반환한다.
     */
    @GetMapping("/login")
    public BaseResponse<Object> loginCallBack(HttpServletRequest request) {
        KakaoLoginResponseDto kakaoLoginResponseDto;
        try {
            // 인가코드로 토큰 발급
            KakaoTokenResponseDto kakaoToken = memberService.getKaKaoToken(request.getParameter("code"));
            // 토큰으로 카카오 사용자 정보 요청 (channelId, email, gender)
            kakaoLoginResponseDto = memberService.getMemberInfoWithToken(kakaoToken);
        } catch (BaseException e) {
            return responseService.getFailureResponse(e.status);
        }

        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto(kakaoLoginResponseDto.getId());
        // 사용자 channelId로 회원가입 여부 확인
        try {
            memberService.getMemberInfoWithChannelId(memberLoginRequestDto.getChannelId());
        } catch (BaseException e) {
            // DB에 존재하지 않는 사용자면 회원가입
            return responseService.getSuccessStatusResponse(BaseResponseStatus.JOIN_SUCCESS, memberService.join(kakaoLoginResponseDto));
        }

        // DB에 존재하는 사용자면 로그인
        MemberLoginResponseDto memberLoginResponseDto;
        try {
            memberLoginResponseDto = memberService.login(memberLoginRequestDto.getChannelId());
            // DB에 가입했지만 이름을 입력하지 않은 사용자면 UUID만 반환해서 이름 입력 창으로 전환
            if (memberLoginResponseDto.getMember().getName().equals("사용자"))
                return responseService.getSuccessStatusResponse(BaseResponseStatus.JOINED_BUT_NO_NAME, new MemberJoinResponseDto(memberLoginResponseDto.getMember().getId()));
        } catch (BaseException e) {
            return responseService.getFailureResponse(BaseResponseStatus.LOGIN_FAILED);
        }
        return responseService.getSuccessStatusResponse(BaseResponseStatus.LOGIN_SUCCESS, memberLoginResponseDto);
    }

    /**
     * 사용자 이름 수정 요청을 처리한다.
     *
     * @param memberUpdateRequestDto 이름 수정을 요청하는 사용자의 id와 수정할 이름을 입력받는다.
     * @return 이름이 수정된 사용자의 회원 정보를 반환한다.
     */
    @PostMapping("/update")
    public BaseResponse<Object> updateName(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        try {
            return responseService.getSuccessResponse(memberService.updateMemberName(memberUpdateRequestDto));
        } catch (BaseException e) {
            return responseService.getFailureResponse(BaseResponseStatus.MEMBER_NAME_UPDATE_FAILED);
        }
    }
}
