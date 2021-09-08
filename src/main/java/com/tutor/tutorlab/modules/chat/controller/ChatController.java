package com.tutor.tutorlab.modules.chat.controller;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.service.ChatService;
import com.tutor.tutorlab.modules.chat.service.MessageService;
import com.tutor.tutorlab.modules.chat.vo.Message;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private MessageService messageService;

    @ApiOperation("채팅방 조회")
    @GetMapping("/a")
    public ResponseEntity getChatInfo() {

        List<Message> msg = messageService.findChatRoom();
        return new ResponseEntity(msg, HttpStatus.OK);
    }
}
