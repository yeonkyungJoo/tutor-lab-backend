package com.tutor.tutorlab.modules.inquiry.vo;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.inquiry.enums.InquiryType;
import lombok.*;

import javax.persistence.*;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "inquiry_id"))
@Getter
//@Setter
@Entity
public class Inquiry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_INQUIRY_USER_ID"))
    private User user;

    // TODO - 입력 체크
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InquiryType type;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    // TODO - 이미지

    @Builder(access = AccessLevel.PRIVATE)
    private Inquiry(User user, InquiryType type, String title, String content) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.content = content;
    }

    public static Inquiry of(User user, InquiryType type, String title, String content) {
        return Inquiry.builder()
                .user(user)
                .type(type)
                .title(title)
                .content(content)
                .build();
    }

}
