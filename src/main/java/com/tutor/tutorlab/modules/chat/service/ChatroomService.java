package com.tutor.tutorlab.modules.chat.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.chat.controller.response.ChatroomResponse;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.repository.MessageRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.CHATROOM;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTEE;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTOR;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChatroomService extends AbstractService {

    private final ChatroomRepository chatroomRepository;
    private final TuteeRepository tuteeRepository;
    private final TutorRepository tutorRepository;
    private final MessageRepository messageRepository;

    public Page<Chatroom> getChatroomsOfTutee(User user, Integer page) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
            .orElseThrow(() -> new UnauthorizedException(TUTEE));
        return chatroomRepository.findByTutee(tutee, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    // TODO - CHECK : Fetch join
    public Page<ChatroomResponse> getChatroomResponsesOfTutee(User user, Integer page) {
        return getChatroomsOfTutee(user, page)
            .map(chatroom -> {
                ChatroomResponse chatroomResponse = new ChatroomResponse(chatroom);
                chatroomResponse.setLastMessage(messageRepository.findFirstByChatroomIdOrderByIdDesc(chatroom.getId()));
                return chatroomResponse;
            });
    }

    public List<Message> getMessagesOfTuteeChatroom(User user, Long chatroomId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Chatroom chatroom = chatroomRepository.findByTuteeAndId(tutee, chatroomId)
                .orElseThrow(() -> new EntityNotFoundException(CHATROOM));

        return messageRepository.findAllByChatroomId(chatroomId);
    }

    public Page<Chatroom> getChatroomsOfTutor(User user, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));
        return chatroomRepository.findByTutor(tutor, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    // TODO - CHECK : Fetch join
    public Page<ChatroomResponse> getChatroomResponsesOfTutor(User user, Integer page) {
        return getChatroomsOfTutor(user, page)
                .map(chatroom -> {
                    ChatroomResponse chatroomResponse = new ChatroomResponse(chatroom);
                    chatroomResponse.setLastMessage(messageRepository.findFirstByChatroomIdOrderByIdDesc(chatroom.getId()));
                    return chatroomResponse;
                });
    }

    public List<Message> getMessagesOfTutorChatroom(User user, Long chatroomId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Chatroom chatroom = chatroomRepository.findByTutorAndId(tutor, chatroomId)
                .orElseThrow(() -> new EntityNotFoundException(CHATROOM));

        return messageRepository.findAllByChatroomId(chatroomId);
    }
}
