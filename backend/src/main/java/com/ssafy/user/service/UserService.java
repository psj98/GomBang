package com.ssafy.user.service;

import com.ssafy.user.domain.User;
import com.ssafy.user.dto.UserJoinRequestDto;
import com.ssafy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean join(UserJoinRequestDto userJoinRequestDto) {
        try {
            User user = userRepository.findByName(userJoinRequestDto.getUserName()).get();
        } catch (NullPointerException e) {
         return false;
        }

        return true;
    }
}
