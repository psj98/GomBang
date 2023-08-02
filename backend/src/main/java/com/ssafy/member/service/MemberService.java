package com.ssafy.member.service;

import com.ssafy.member.domain.Member;
import com.ssafy.member.dto.KakaoLoginResponseDto;
import com.ssafy.member.dto.MemberUpdateRequestDto;
import com.ssafy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    // 카카오 인가코드로 토큰 발급 요청
    public String getKaKaoToken(String code) throws Exception {
        // 인가코드가 있는지 확인
        if (code == null)
            throw new Exception("Failed get authorization code");

        String accessToken = "";
        String refreshToken = "";

        try {
            // 카카오로 보낼 헤더 만들기
            HttpHeaders headers = new HttpHeaders();
            // 서버로 보내기 전 인코딩
            headers.add("Content-type", "application/x-www-form-urlencoded");

            // 카카오로 보낼 바디 만들기
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("redirect_uri", KAKAO_REDIRECT_URL);
            params.add("code", code);

            // REST방식으로 API를 호출할 수 있는 내장 클래스
            // 스프링 3.0부터 지원인데 버전 업 안해도 정상작동하는지 확인 필요
            RestTemplate restTemplate = new RestTemplate();
            // 카카오로 전송하기 위해 헤더와 바디 합치기
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            // POST방식으로 카카오에 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            // Response 데이터 파싱
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            // 데이터에서 토큰 빼오기
            accessToken = (String) jsonObj.get("access_token");
            refreshToken = (String) jsonObj.get("refresh_token");
        } catch (Exception e) {
            throw new Exception("API call failed");
        }

        // 토큰 반환
        return accessToken;
    }

    // 토큰으로 카카오 사용자 정보 요청
    public KakaoLoginResponseDto getMemberInfoWithToken(String accessToken) throws ParseException {
        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader 담아서 카카오로 POST요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        String channelId = (String) jsonObj.get("id");
        String email = String.valueOf(account.get("email"));
        String gender = String.valueOf(account.get("gender"));

        // channelId, email, gender 데이터 반환
        return KakaoLoginResponseDto.builder()
                .channelId(channelId)
                .email(email)
                .gender(gender).build();
    }

    // 들어온 사용자 정보 + 생성한 UUID/닉네임으로 회원가입 (DB에 저장)
    @Transactional
    public Member join(KakaoLoginResponseDto kakaoLoginResponseDto) {
        // UUID 생성
        UUID id = generateUUID();
        String nickname = "회원가입한 곰돌이";
        // 닉네임 랜덤 생성 및 중복 검사 메서드 추후 작성 필요

        // 회원가입한 사용자 정보
        Member member =  Member.builder()
                .id(id)
                .channelId(kakaoLoginResponseDto.getChannelId())
                .nickname(nickname)
                .email(kakaoLoginResponseDto.getEmail())
                .gender(kakaoLoginResponseDto.getGender()).build();
        
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
