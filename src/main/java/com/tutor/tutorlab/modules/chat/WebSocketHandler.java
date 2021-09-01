package com.tutor.tutorlab.modules.chat;

import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, Map<String, WebSocketSession>> chatroomMap = new HashMap<>();
    private final ChatroomRepository chatroomRepository;

    @PostConstruct
    private void init() {
//        List<Chatroom> chatrooms = chatroomRepository.findAll();
//        chatrooms.stream().forEach(chatroom -> {
//            chatroomMap.put(chatroom.getId(), new HashMap<>());
//        });

        chatroomMap.put(1L, new HashMap<>());
        chatroomMap.put(2L, new HashMap<>());
        chatroomMap.put(3L, new HashMap<>());
    }

    // 소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.info("------------ Connection Establised ------------");
        super.afterConnectionEstablished(session);

        String uri = session.getUri().toString();
        // TODO - or getQuery : ?chatroomId=1
        Long chatroomId = Long.valueOf(uri.split("/chat/")[1]);

        if (chatroomMap.containsKey(chatroomId)) {  // 방이 존재
            // TODO - CHECK
            Map<String, WebSocketSession> sessionMap = chatroomMap.get(chatroomId);
            sessionMap.put(session.getId(), session);
        } else {
            // throw Exception
        }

        JSONObject object = new JSONObject();
        object.put("type", "sessionId");
        object.put("sessionId", session.getId());

        session.sendMessage(new TextMessage(object.toJSONString()));
        log.info("object : {}", object.toJSONString());
    }

    // 메세지 발송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String text = message.getPayload();
        /*
            {
                "type" : "message",
                "chatroomId" : "1",
                "sessionId" : "e628d45c-21a7-4d1f-7349-9b3c623f8b38",
                "username" : "user1",
                "message" : "hi~"
            }
        */
        JSONObject object = JsonUtil.parse(text);
        log.info("text : {}", object.toJSONString());
        Long chatroomId = Long.valueOf((String)object.get("chatroomId"));

        // 해당 방의 세션에만 메세지 발송
        Map<String, WebSocketSession> sessionMap = chatroomMap.get(chatroomId);
        for (String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            log.info("chatroomId : {}, sessionId : {}, object : {}", chatroomId, key, object.toJSONString());
            wss.sendMessage(new TextMessage(object.toJSONString()));
        }
    }

    // 소켓 종료
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
