package com.ssafy.member.service;

import com.ssafy.member.domain.Gender;
import com.ssafy.member.domain.Member;
import com.ssafy.member.dto.KakaoLoginResponseDto;
import com.ssafy.member.dto.KakaoTokenResponseDto;
import com.ssafy.member.dto.MemberUpdateRequestDto;
import com.ssafy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com/v2/user/me";

    // 카카오 인가코드로 토큰 발급 요청
    public KakaoTokenResponseDto getKaKaoToken(String code) {
        // 인가코드가 있는지 확인
        if (code == null)
//            throw new Exception("Failed get authorization code");
            System.out.println("코드없음");

        // 카카오로 보낼 바디 만들기
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_SECRET);
        params.add("redirect_uri", KAKAO_REDIRECT_URL);
        params.add("code", code);

        // API 호출
        // POST방식으로 카카오에 요청 보내기
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create()
                .post()
                .uri(KAKAO_AUTH_URI)
                .header("Content-type","application/x-www-form-urlencoded;charset=utf-8" )
                .bodyValue(params)
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        // 토큰 반환
        return kakaoTokenResponseDto;
    }

    // 토큰으로 카카오 사용자 정보 요청
    public KakaoLoginResponseDto getMemberInfoWithToken(KakaoTokenResponseDto kakaoTokenResponseDto) {
        // API 호출
        // POST방식으로 카카오에 요청 보내기
        KakaoLoginResponseDto kakaoLoginResponseDto = WebClient.create()
                .post()
                .uri(KAKAO_API_URI)
                .headers(header -> header.setBearerAuth(kakaoTokenResponseDto.getAccess_token()))
                .retrieve()
                .bodyToMono(KakaoLoginResponseDto.class)
                .block();

        // channelId, email, gender 데이터 반환
        return kakaoLoginResponseDto;
    }

    // 들어온 사용자 정보 + 생성한 UUID/닉네임으로 회원가입 (DB에 저장)
    @Transactional
    public Member join(KakaoLoginResponseDto kakaoLoginResponseDto) {
        // UUID 생성
        UUID id = generateUUID();
        String nickname = "회원가입한 곰돌이";
        // 닉네임 랜덤 생성 및 중복 검사 메서드 추후 작성 필요

        // 성별 제공에 동의했는지 확인 하고 성별에 맞는 enum타입으로 변환
        Gender gender = Gender.MALE;
        if (kakaoLoginResponseDto.getKakao_account().getGender() == null)
            gender = Gender.NULL;
        else if (kakaoLoginResponseDto.getKakao_account().getGender().equals("female"))
            gender = Gender.FEMALE;

        // 회원가입한 사용자 정보
        Member member = Member.builder()
                .id(id)
                .channelId(kakaoLoginResponseDto.getId())
                .nickname(nickname)
                .email(kakaoLoginResponseDto.getKakao_account().getEmail())
                .gender(gender).build();

        // DB에 회원가입한 사용자 정보 저장
        memberRepository.save(member);
        return member;
    }

    // 사용자 이름 수정
    @Transactional
    public Member updateMemberName(MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = getMemberInfoWithId(memberUpdateRequestDto.getMember().getId());
        member.setName(memberUpdateRequestDto.getMember().getName());
        return member;
    }

    // channelId 기준으로 회원 정보 조회
    public Member getMemberInfoWithChannelId(String channelId) {
        try {
            return memberRepository.findByChannelId(channelId).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Cannot find member by channelId");
        }
    }

    // id 기준으로 회원 정보 조회
    public Member getMemberInfoWithId(UUID id) {
        try {
            return memberRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Cannot find member by id");
        }
    }

    // 중복되지 않는 UUID 생성
    private UUID generateUUID() {
        UUID id = UUID.randomUUID();
        while (checkUuidDuplicate(id)) {
            id = UUID.randomUUID();
        }
        return id;
    }

    // UUID 기준으로 중복 회원 확인
    private boolean checkUuidDuplicate(UUID id) {
        try {
            Member member = memberRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }
}
