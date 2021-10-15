package com.tutor.tutorlab.modules.chat.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"ChatroomController"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatroomController {

}
