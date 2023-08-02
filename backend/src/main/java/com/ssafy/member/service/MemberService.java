package com.ssafy.member.service;

import com.ssafy.member.domain.Member;
import com.ssafy.member.dto.MemberJoinRequestDto;
import com.ssafy.member.dto.MemberLoginRequestDto;
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
    public MemberLoginRequestDto getKaKaoInfo(String code) throws Exception {
        // 인가코드가 있는지 확인
        if (code == null)
            throw new Exception("인가코드가 없어요~~~~~~~`");

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
            // 스프링 3.0부터 지원인데 버전 업 안해도 정상작동하는지 확인
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
            throw new Exception("API 호출 실패");
        }

        getUserInfoWithToken(accessToken);
        // 유저 정보 받은걸로 가입 여부 확인
        // uuid 생성
        // 닉네임 생성 - 중복 확인
        // 이름 디폴트 생성해서 넘겨주기



        return;
    }

    // 토큰으로 카카오 사용자 정보 요청
    private MemberLoginRequestDto getUserInfoWithToken(String accessToken) throws ParseException {
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

        // channelId,email, gender 데이터 반환
        return MemberLoginRequestDto.builder()
                .channelId(channelId)
                .email(email)
                .gender(gender).build();
    }

    public boolean join(MemberJoinRequestDto userJoinRequestDto) {
        try {
            Member member = memberRepository.findByName(userJoinRequestDto.getUserName()).get();
        } catch (NullPointerException e) {
         return false;
        }

        return true;
    }
}
