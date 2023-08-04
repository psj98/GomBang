package com.ssafy.chat.service;

import com.ssafy.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
}
