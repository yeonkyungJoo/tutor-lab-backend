package com.tutor.tutorlab.modules.notification.vo;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.notification.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
@AttributeOverride(name = "id", column = @Column(name = "notification_id"))
@Entity
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_NOTIFICATION_USER_ID"))
    private User user;  // 수신인

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String contents;

    private boolean checked = false;    // 확인 여부
    private LocalDateTime checkedAt;

    public void check() {
        if (!checked) {
            setChecked(true);
            setCheckedAt(LocalDateTime.now());
        }
    }

}
