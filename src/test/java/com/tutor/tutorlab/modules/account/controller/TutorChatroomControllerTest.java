package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.controller.response.ChatroomResponse;
import com.tutor.tutorlab.modules.chat.enums.MessageType;
import com.tutor.tutorlab.modules.chat.service.ChatroomService;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TutorChatroomControllerTest {

    private final static String BASE_URL = "/api/tutors/my-chatrooms";

    @InjectMocks
    TutorChatroomController tutorChatroomController;
    @Mock
    ChatroomService chatroomService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tutorChatroomController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @Test
    void getChatrooms() throws Exception {

        // given
        Enrollment enrollment1 = mock(Enrollment.class);
        when(enrollment1.getLecture()).thenReturn(mock(Lecture.class));
        Tutor tutor1 = mock(Tutor.class);
        when(tutor1.getUser()).thenReturn(mock(User.class));
        Tutee tutee1 = mock(Tutee.class);
        when(tutee1.getUser()).thenReturn(mock(User.class));
        Chatroom chatroom1 = Chatroom.of(enrollment1, tutor1, tutee1);

        Enrollment enrollment2 = mock(Enrollment.class);
        when(enrollment2.getLecture()).thenReturn(mock(Lecture.class));
        Tutor tutor2 = mock(Tutor.class);
        when(tutor2.getUser()).thenReturn(mock(User.class));
        Tutee tutee2 = mock(Tutee.class);
        when(tutee2.getUser()).thenReturn(mock(User.class));
        Chatroom chatroom2 = Chatroom.of(enrollment2, tutor2, tutee2);

        Page<ChatroomResponse> chatrooms = new PageImpl<>(Arrays.asList(new ChatroomResponse(chatroom1), new ChatroomResponse(chatroom2)), Pageable.ofSize(20), 2);
        doReturn(chatrooms).when(chatroomService).getChatroomResponsesOfTutor(any(User.class), anyInt());

        // when
        // then
        mockMvc.perform(get(BASE_URL, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(chatrooms)));
    }

    @Test
    void getMessagesOfChatroom() throws Exception {

        // given
        User user = User.of(
                "user@email.com",
                "password",
                "user", null, null, null, "user@email.com",
                "user", null, null, null, RoleType.TUTEE,
                null, null
        );
        PrincipalDetails principal = new PrincipalDetails(user);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities()));

        Message message1 = Message.of(MessageType.MESSAGE, 1L, "sessionId",
                "user", 1L, "message1", LocalDateTime.now(), false);
        Message message2 = Message.of(MessageType.MESSAGE, 1L, "sessionId",
                "user", 1L, "message2", LocalDateTime.now(), false);
        List<Message> messages = Arrays.asList(message1, message2);
        doReturn(messages)
                .when(chatroomService).getMessagesOfTutorChatroom(user, 1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{chatroom_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(messages)));
    }
}