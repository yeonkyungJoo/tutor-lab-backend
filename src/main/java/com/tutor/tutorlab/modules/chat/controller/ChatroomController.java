package com.tutor.tutorlab.modules.chat.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.service.ChatService;
import com.tutor.tutorlab.modules.chat.service.ChatroomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"ChatroomController"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatroomController {

    private final ChatroomService chatroomService;

    @ApiOperation("채팅방/상대 신고")
    @PutMapping("/{chatroom_id}/accuse")
    public ResponseEntity<?> accuse(@CurrentUser User user,
                                    @PathVariable(name = "chatroom_id") Long chatroomId) {
        chatroomService.accuse(user, chatroomId);
        return ResponseEntity.ok().build();
    }

}
