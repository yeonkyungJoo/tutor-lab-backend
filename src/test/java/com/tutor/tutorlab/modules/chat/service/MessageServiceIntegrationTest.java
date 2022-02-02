package com.tutor.tutorlab.modules.chat.service;

import com.tutor.tutorlab.modules.chat.enums.MessageType;
import com.tutor.tutorlab.modules.chat.repository.MessageRepository;
import com.tutor.tutorlab.modules.chat.vo.Message;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
class MessageServiceIntegrationTest {

    @Autowired
    MessageService messageService;
    @Autowired
    MessageRepository messageRepository;

    @Test
    void saveMessage() {

        // given
        // when
        // then
        Message message = Message.of(
                MessageType.MESSAGE,
                1L,
                "4253d14c-c3d5-ef9d-22cb-8823c7632c24",
                "user1",
                1L,
                "hi",
                LocalDateTime.now(),
                true
        );
        messageService.saveMessage(message);
//        System.out.println(messageRepository.findAll());
    }
}