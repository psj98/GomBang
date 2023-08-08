package com.ssafy.member.controller;


import com.ssafy.global.common.response.BaseResponse;
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
     * 곰방 로그인의 경우 회원 정보를 반환하고, 회원가입의 경우와 회원가입 이후 사용자 이름을 입력하지 않은 경우 UUID를 반환한다.
     *
     * request_url=https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=a20ef37212e1ae86b20e09630f6590ce&redirect_uri=http://localhost:3000/auth
     * @param kakaoLoginRequestDto 카카오 인가토큰을 입력받는다.
     * @return 로그인하는 경우 : 회원 정보를 반환한다.
     * @return 회원가입하는 경우 : 새롭게 생성된 UUID를 반환한다.
     * @return 회원가입 후 사용자 이름을 입력하지 않은 경우 : 회원가입 시 생성된 UUID를 반환한다.
     */
    @GetMapping("/login")
    public BaseResponse<Object> loginCallBack(@RequestBody KakaoLoginRequestDto kakaoLoginRequestDto) {
        // 인가코드로 토큰 발급
        KakaoTokenResponseDto kakaoToken = memberService.getKaKaoToken(kakaoLoginRequestDto.getCode());
        // 토큰으로 카카오 사용자 정보 요청 (channelId, email, gender)
        KakaoLoginResponseDto kakaoLoginResponseDto = memberService.getMemberInfoWithToken(kakaoToken);
        MemberLoginResponseDto memberLoginResponseDto = null;
        // 사용자 channelId로 회원가입 여부 확인
        try {
            // DB에 존재하는 사용자면 로그인
            memberLoginResponseDto = new MemberLoginResponseDto(memberService.getMemberInfoWithChannelId(kakaoLoginResponseDto.getId()));
            // 회원가입은 됐지만 이름 입력이 안된 사용자면 UUID와 함께 이름 입력 화면으로 반환
            if (memberLoginResponseDto.getMember().getName().equals("사용자"))
                throw new IllegalAccessException();
            return responseService.getSuccessResponse("로그인 성공", memberLoginResponseDto);
        } catch (NoSuchElementException e) {
            // DB에 존재하지 않는 사용자면 회원가입
            MemberJoinResponseDto memberJoinResponseDto = new MemberJoinResponseDto(memberService.join(kakaoLoginResponseDto).getId());
            return responseService.getSuccessResponse("회원가입 성공", memberJoinResponseDto);
        } catch (IllegalAccessException e) {
            // DB에 가입했지만 이름을 입력하지 않은 사용자면 UUID만 반환해서 이름 입력 창으로 전환
            MemberJoinResponseDto memberJoinResponseDto = new MemberJoinResponseDto(memberLoginResponseDto.getMember().getId());
            return responseService.getSuccessResponse("이름 입력 안된 사용자", memberJoinResponseDto);
        }
    }

    /**
     * 회원 정보의 이름을 수정 요청을 처리한다.
     *
     * @param memberUpdateRequestDto 이름 수정을 요청하는 사용자의 id와 수정할 이름을 입력받는다.
     * @return 이름이 수정된 사용자의 회원 정보를 반환한다.
     */
    @PostMapping("/update")
    public BaseResponse<Object> updateName(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        MemberLoginResponseDto memberLoginResponseDto = new MemberLoginResponseDto(memberService.updateMemberName(memberUpdateRequestDto));
        return responseService.getSuccessResponse("이름 수정 성공", memberLoginResponseDto);
    }
}
