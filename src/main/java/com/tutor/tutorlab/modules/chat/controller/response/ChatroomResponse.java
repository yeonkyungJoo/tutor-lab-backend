package com.tutor.tutorlab.modules.chat.controller.response;

import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import lombok.Data;

@Data
public class ChatroomResponse {

    // TODO - 쿼리
    // TODO - FETCH JOIN
    public ChatroomResponse(Chatroom chatroom) {
        this.chatroomId = chatroom.getId();
        this.lectureTitle = chatroom.getEnrollment().getLecture().getTitle();
        this.tutorId = chatroom.getTutor().getUser().getId();
        // this.tutorUsername = chatroom.getTutor().getUser().getUsername();
        this.tutorNickname = chatroom.getTutor().getUser().getNickname();
        this.tutorImage = chatroom.getTutor().getUser().getImage();
        this.tuteeId = chatroom.getTutee().getUser().getId();
        // this.tuteeUsername = chatroom.getTutee().getUser().getUsername();
        this.tuteeNickname = chatroom.getTutee().getUser().getNickname();
        this.tuteeImage = chatroom.getTutee().getUser().getImage();
    }

    private Long chatroomId;
    private String lectureTitle;
    private Long tutorId;
    // private String tutorUsername;
    private String tutorNickname;
    private String tutorImage;
    private Long tuteeId;
    // private String tuteeUsername;
    private String tuteeNickname;
    private String tuteeImage;
    private Message lastMessage;
    private Integer uncheckedMessageCount = 0;
}
