package com.tutor.tutorlab.modules.chat.controller.response;

import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import lombok.Data;

@Data
public class ChatroomResponse {

    public ChatroomResponse(Chatroom chatroom) {
        this.chatroomId = chatroom.getId();
        this.lectureTitle = chatroom.getEnrollment().getLecture().getTitle();
        this.tutorId = chatroom.getTutor().getUser().getId();
        this.tutorImage = chatroom.getTutor().getUser().getImage();
        this.tuteeId = chatroom.getTutee().getUser().getId();
        this.tuteeImage = chatroom.getTutee().getUser().getImage();
    }

    private Long chatroomId;
    // private User tutor;
    // private User tutee;
    private String lectureTitle;
    private Long tutorId;
    private String tutorImage;
    private Long tuteeId;
    private String tuteeImage;
    private Message lastMessage;
}
