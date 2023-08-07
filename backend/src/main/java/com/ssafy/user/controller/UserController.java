//package com.ssafy.user.controller;
//
//
//import com.ssafy.common.model.response.BaseResponseBody;
//import com.ssafy.global.common.response.BaseResponse;
//import com.ssafy.global.common.response.ResponseService;
//import com.ssafy.user.domain.User;
//import com.ssafy.user.dto.UserJoinRequestDto;
//import com.ssafy.user.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/user")
//public class UserController {
//
//    private final ResponseService responseService;
//    private final UserService userService;
//
//    @PostMapping("/join")
//    public BaseResponse<Object> join(@RequestBody UserJoinRequestDto userJoinRequestDto) {
//        return responseService.getSuccessResponse("회원가입 성공", userService.join(userJoinRequestDto));
//    }
//}
