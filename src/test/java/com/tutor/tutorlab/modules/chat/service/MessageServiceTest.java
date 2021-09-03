package com.tutor.tutorlab.modules.chat.service;

import com.tutor.tutorlab.modules.chat.enums.MessageType;
import com.tutor.tutorlab.modules.chat.repository.MessageRepository;
import com.tutor.tutorlab.modules.chat.vo.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    MessageService messageService;
//    @Autowired
//    MessageRepository messageRepository;

    @Test
    void saveMessage() {
        /*
        {
            "type" : "message",
            "chatroomId" : 1L,
            "sessionId" : "4253d14c-c3d5-ef9d-22cb-8823c7632c24",
            "username" : "user1",
            "message" : "hi~"
        }
        */
        Message message = new Message();
        message.setType(MessageType.MESSAGE);
        message.setChatroomId(1L);
        message.setSessionId("4253d14c-c3d5-ef9d-22cb-8823c7632c24");
        message.setUsername("user1");
        message.setMessage("hi!");

        messageService.saveMessage(message);
//        System.out.println(messageRepository.findAll());
    }
}