package com.tutor.tutorlab.modules.chat;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.enums.MessageType;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.service.MessageService;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import com.tutor.tutorlab.modules.firebase.service.AndroidPushNotificationsService;
import com.tutor.tutorlab.modules.notification.enums.NotificationType;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
import com.tutor.tutorlab.utils.JsonUtil;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.CHATROOM;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.USER;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // TODO - CHECK
    private static final String TYPE = "type";
    private static final String SESSION_ID = "sessionId";
    private static final String CHATROOM_ID = "chatroomId";
    private static final String SENDER_NICKNAME = "senderNickname";
    private static final String RECEIVER_ID = "receiverId";
    private static final String MESSAGE = "message";


    // TODO - CHECK
    public static final Map<Long, Map<String, WebSocketSession>> chatroomMap = new HashMap<>();
    private final ChatroomRepository chatroomRepository;

    private final MessageService messageService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final AndroidPushNotificationsService androidPushNotificationsService;

    @PostConstruct
    private void init() {

        List<Chatroom> chatrooms = chatroomRepository.findAll();
        // TODO - CHECK : forEach & static
        chatrooms.stream().forEach(chatroom -> {
            chatroomMap.put(chatroom.getId(), new HashMap<>());
        });
    }

    // ?????? ??????
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String uri = session.getUri().toString();

        // TODO - or getQuery : ?chatroomId=1
        Long chatroomId = Long.valueOf(uri.split("/chat/")[1]);
        if (chatroomMap.containsKey(chatroomId)) {

            Map<String, WebSocketSession> sessionMap = chatroomMap.get(chatroomId);
            sessionMap.put(session.getId(), session);

            log.info("------------ Connection Establised ------------");
            super.afterConnectionEstablished(session);

            JSONObject object = new JSONObject();
            object.put(TYPE, SESSION_ID);
            object.put(SESSION_ID, session.getId());

            session.sendMessage(new TextMessage(object.toJSONString()));
            log.info(object.toJSONString());

        } else {
            throw new EntityNotFoundException(CHATROOM);
        }
    }

    // ????????? ??????
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String text = message.getPayload();

        JSONObject object = JsonUtil.parse(text);
        log.info(object.toJSONString());

        if (object.get(CHATROOM_ID) != null) {
//            {
//                "chatroomId": 42,
//                "senderNickname": "user1",
//                "receiverId": 60,
//                "message": "hello"
//            }
            Long chatroomId = (Long) object.get(CHATROOM_ID);

            // ?????? ?????? ???????????? ????????? ??????
            Map<String, WebSocketSession> sessionMap = chatroomMap.get(chatroomId);
            for (String key : sessionMap.keySet()) {
                WebSocketSession wss = sessionMap.get(key);
                wss.sendMessage(new TextMessage(object.toJSONString()));
            }

            Long receiverId = (Long) object.get(RECEIVER_ID);
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new EntityNotFoundException(USER));
            // TODO - CHECK : ????????? ?????? - ????????? ????????????
            // TODO - CHECK : ????????? ??????
            if (sessionMap.size() != 2) {
                notificationService.createNotification(receiverId, NotificationType.CHAT);
            }
            String sender = (String) object.get(SENDER_NICKNAME);
            String messageText = (String) object.get(MESSAGE);
            Message msg = Message.of(
                    MessageType.MESSAGE,
                    chatroomId,
                    session.getId(),
                    sender,                         // ????????? (?????????)
                    receiverId,                     // ????????? (?????????)
                    messageText,
                    LocalDateTime.now(),
                    sessionMap.size() == 2
            );
            messageService.saveMessage(msg);
            // androidPushNotificationsService.send(receiver.getFcmToken(), sender + "??????????????? ????????? ??????????????????", messageText);
        }
    }

    // ?????? ??????
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info("------------ Connection Closed ------------");
        Collection<Map<String, WebSocketSession>> values = chatroomMap.values();
        for (Map<String, WebSocketSession> sessionMap : values) {
            if (sessionMap.containsKey(session.getId())) {
                sessionMap.remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }
}
