package com.tutor.tutorlab.modules.chat.controller;

import com.tutor.tutorlab.modules.account.controller.AbstractController;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
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
            this.lectureTitle = chatroom.getEnrollment().getLecture().getTitle();
            this.tutorId = chatroom.getTutor().getUser().getId();
            this.tutorImage = chatroom.getTutor().getUser().getImage();
            this.tuteeId = chatroom.getTutee().getUser().getId();
            this.tuteeImage = chatroom.getTutee().getUser().getImage();
        }

        private Long chatroomId;
        // private User tutor;
        // private User tutee;
        private String lectureTitle;
        private Long tutorId;
        private String tutorImage;
        private Long tuteeId;
        private String tuteeImage;
        private Message lastMessage;
    }

}
