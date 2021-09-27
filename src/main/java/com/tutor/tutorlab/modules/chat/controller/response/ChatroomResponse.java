package com.tutor.tutorlab.modules.chat.controller.response;

import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import lombok.Data;

@Data
public class ChatroomResponse {

    // TODO - FETCH JOIN
    public ChatroomResponse(Chatroom chatroom) {
        this.chatroomId = chatroom.getId();
        this.lectureTitle = chatroom.getEnrollment().getLecture().getTitle();
        // this.tutorId = chatroom.getTutor().getUser().getId();
        this.tutorNickname = chatroom.getTutor().getUser().getNickname();
        this.tutorImage = chatroom.getTutor().getUser().getImage();
        // this.tuteeId = chatroom.getTutee().getUser().getId();
        this.tuteeNickname = chatroom.getTutee().getUser().getNickname();
        this.tuteeImage = chatroom.getTutee().getUser().getImage();
    }

    private Long chatroomId;
    // private User tutor;
    // private User tutee;
    private String lectureTitle;
    // private Long tutorId;
    private String tutorNickname;
    private String tutorImage;
    // private Long tuteeId;
    private String tuteeNickname;
    private String tuteeImage;
    private Message lastMessage;
}
