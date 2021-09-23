package com.tutor.tutorlab.modules.chat.controller;

import com.tutor.tutorlab.modules.chat.service.MessageService;
import com.tutor.tutorlab.modules.chat.vo.Message;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final MessageService messageService;

    @ApiOperation("채팅 메시지 리스트")
    @GetMapping("/messages")
    public ResponseEntity<?> getMessages() {

        List<Message> messages = messageService.getMessages();
        return ResponseEntity.ok(messages);
    }

}
