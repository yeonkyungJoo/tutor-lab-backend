package com.tutor.tutorlab.modules.chat.service;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.chat.WebSocketHandler;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatroomRepository chatroomRepository;

    // 강의 수강 시 채팅방 자동 생성
    public void createChatroom(Tutor tutor, Tutee tutee, Enrollment enrollment) {

        Chatroom chatroom = Chatroom.builder()
                .enrollment(enrollment)
                .tutee(tutee)
                .tutor(tutor)
                .build();
        enrollment.setChatroom(chatroom);

        chatroom = chatroomRepository.save(chatroom);
        // TODO - CHECK
        WebSocketHandler.chatroomMap.put(chatroom.getId(), new HashMap<>());
    }

    // 채팅방 삭제
    // - 강의 취소 시 채팅방 자동 삭제
    // - 강의 종료 시 채팅방 자동 삭제
    public void deleteChatroom(Enrollment enrollment) {
        chatroomRepository.deleteByEnrollment(enrollment);
        // TODO - 웹소켓 세션 삭제
    }

}
