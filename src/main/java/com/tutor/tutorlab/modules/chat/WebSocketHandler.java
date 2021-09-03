package com.tutor.tutorlab.modules.chat;

import com.tutor.tutorlab.modules.chat.enums.MessageType;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.service.MessageService;
import com.tutor.tutorlab.modules.chat.vo.Message;
import com.tutor.tutorlab.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, Map<String, WebSocketSession>> chatroomMap = new HashMap<>();
    private final ChatroomRepository chatroomRepository;

    private final MessageService messageService;

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
        
        // TODO - builder로 변경
        Message msg = new Message();
        msg.setType(MessageType.SESSIONID);
        msg.setChatroomId(chatroomId);
        msg.setSessionId(session.getId());
        msg.setMessage("Connection Establised");

        messageService.saveMessage(msg);
        
    }

    // 메세지 발송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String text = message.getPayload();
        /*
            {
                "type" : "message",
                "chatroomId" : 1,
                "sessionId" : "e628d45c-21a7-4d1f-7349-9b3c623f8b38",
                "username" : "user1",
                "message" : "hi~"
            }
        */
        JSONObject object = JsonUtil.parse(text);
        log.info("text : {}", object.toJSONString());

        Long chatroomId = 0L;
        if (object.get("chatroomId") != null) {
            chatroomId = (Long) object.get("chatroomId");
        }

        // 해당 방의 세션에만 메세지 발송
        Map<String, WebSocketSession> sessionMap = chatroomMap.get(chatroomId);
        for (String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            log.info("chatroomId : {}, sessionId : {}, object : {}", chatroomId, key, object.toJSONString());
            wss.sendMessage(new TextMessage(object.toJSONString()));
        }

        Message msg = new Message();
        msg.setType(MessageType.MESSAGE);
        msg.setChatroomId(chatroomId);
        msg.setSessionId(session.getId());
        msg.setMessage((String) object.get("message"));

        messageService.saveMessage(msg);
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
