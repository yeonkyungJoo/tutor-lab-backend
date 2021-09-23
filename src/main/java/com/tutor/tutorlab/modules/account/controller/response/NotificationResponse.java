package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.notification.vo.Notification;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Data;

@Data
public class NotificationResponse {

    public NotificationResponse(Notification notification) {
        // this.username = notification.getUser().getUsername();
        this.contents = notification.getContents();
        this.checked = notification.isChecked();
        this.createdAt = LocalDateTimeUtil.getDateTimeToString(notification.getCreatedAt());
        this.checkedAt = LocalDateTimeUtil.getDateTimeToString(notification.getCheckedAt());
    }

    // private String username;    // 수신인
    private String contents;
    private boolean checked;
    private String createdAt;
    private String checkedAt;
}
