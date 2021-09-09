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

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private MessageType type;
    private Long chatroomId;
    private String sessionId;
    private String username;
    private String message;

}
