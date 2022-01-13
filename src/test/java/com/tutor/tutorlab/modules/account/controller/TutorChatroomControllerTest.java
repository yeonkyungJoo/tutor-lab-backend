package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.controller.response.ChatroomResponse;
import com.tutor.tutorlab.modules.chat.service.ChatroomService;
import com.tutor.tutorlab.modules.chat.vo.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TutorChatroomControllerTest {

    private final static String BASE_URL = "/tutees/my-chatrooms";

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
        Page<ChatroomResponse> chatrooms =
                new PageImpl<>(Arrays.asList(Mockito.mock(ChatroomResponse.class), Mockito.mock(ChatroomResponse.class)), Pageable.ofSize(20), 2);
        doReturn(chatrooms)
                .when(chatroomService).getChatroomResponsesOfTutor(any(User.class), 1);
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
        List<Message> messages = Arrays.asList(Mockito.mock(Message.class), Mockito.mock(Message.class));
        doReturn(messages)
                .when(chatroomService).getMessagesOfTutorChatroom(any(User.class), 1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{chatroom_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(messages)));
    }
}