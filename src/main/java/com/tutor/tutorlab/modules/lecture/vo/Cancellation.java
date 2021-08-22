package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AttributeOverride(name = "id", column = @Column(name = "cancellation_id"))
@NoArgsConstructor
@Getter @Setter
@Entity
public class Cancellation extends BaseEntity {

    // 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id",
            referencedColumnName = "tutee_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CANCELLATION_TUTEE_ID"))
    private Tutee tutee;

    // 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id",
            referencedColumnName = "lecture_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CANCELLATION_LECTURE_ID"))
    private Lecture lecture;

    private LocalDateTime enrolledAt;
    // private String reason;

    @Builder
    public Cancellation(Tutee tutee, Lecture lecture, LocalDateTime enrolledAt) {
        this.tutee = tutee;
        this.lecture = lecture;
        this.enrolledAt = enrolledAt;
    }
}
