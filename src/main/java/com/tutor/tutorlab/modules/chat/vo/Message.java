package com.tutor.tutorlab.modules.chat.vo;

import com.tutor.tutorlab.modules.chat.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Document(collection = "messages")
@NoArgsConstructor
@Getter @Setter
public class Message {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private MessageType type;
    private Long chatroomId;
    private String sessionId;
    private String username;
    private String message;
    // private LocalDateTime sentAt;
    private String sentAt;

    private boolean checked;

    @Builder
    public Message(MessageType type, Long chatroomId, String sessionId, String username, String message, String sentAt, boolean checked) {
        this.type = type;
        this.chatroomId = chatroomId;
        this.sessionId = sessionId;
        this.username = username;
        this.message = message;
        this.sentAt = sentAt;
        this.checked = checked;
    }
}
