package com.tutor.tutorlab.modules.chat.service;

import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatroomRepository chatroomRepository;

    // 채팅방 생성
    // - 강의 수강 시 채팅방 자동 생성
    public void createChatroom() {

    }

    // 채팅방 삭제
    // - 강의 취소 시 채팅방 자동 삭제
    // - 강의 종료 시 채팅방 자동 삭제
    public void deleteChatroom() {

    }
}
