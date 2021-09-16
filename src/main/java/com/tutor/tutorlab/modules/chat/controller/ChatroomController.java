package com.tutor.tutorlab.modules.chat.controller;

import com.tutor.tutorlab.modules.account.controller.AbstractController;
import com.tutor.tutorlab.modules.chat.repository.MessageRepository;
import com.tutor.tutorlab.modules.chat.service.ChatroomService;
import com.tutor.tutorlab.modules.chat.service.MessageService;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatroomController extends AbstractController {

    private final ChatroomService chatroomService;

    @Data
    public static class ChatroomDto {

        public ChatroomDto(Chatroom chatroom) {
            this.chatroomId = chatroom.getId();
            this.tutorName = chatroom.getTutor().getUser().getName();
            this.tuteeName = chatroom.getTutee().getUser().getName();
        }

        private Long chatroomId;
        private String tutorName;
        private String tuteeName;
        private Message lastMessage;
    }

}
