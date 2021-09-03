package com.tutor.tutorlab.modules.test.controller;

import com.tutor.tutorlab.modules.chat.repository.MessageRepository;
import com.tutor.tutorlab.modules.chat.service.MessageService;
import com.tutor.tutorlab.modules.chat.vo.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {

    private final MessageRepository messageRepository;
    private final MessageService messageService;

    @GetMapping("/exception")
    public ResponseEntity getErrorTest() throws Exception {
        boolean errorFlag = true;
        if (Boolean.TRUE.equals(errorFlag)) {
            throw new Exception("throws Exception");
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
