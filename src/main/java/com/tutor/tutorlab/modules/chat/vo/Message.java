package com.tutor.tutorlab.modules.chat.vo;

import com.tutor.tutorlab.modules.chat.enums.MessageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Document(collection = "messages")
@Getter
@Setter
public class Message {

    /*
    {
        "type" : "message",
        "chatroomId" : 1L,
        "sessionId" : "4253d14c-c3d5-ef9d-22cb-8823c7632c24",
        "username" : "user1",
        "message" : "hi~"
    }
    */
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    private Long chatroomId;
    private String sessionId;
    private String username;
    private String message;



}
