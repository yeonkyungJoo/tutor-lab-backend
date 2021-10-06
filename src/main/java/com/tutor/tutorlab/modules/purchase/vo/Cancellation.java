package com.tutor.tutorlab.modules.purchase.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "cancellation_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    // 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_price_id",
            referencedColumnName = "lecture_price_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CANCELLATION_LECTURE_PRICE_ID"))
    private LecturePrice lecturePrice;

    private LocalDateTime enrolledAt;
    // private String reason;

    @Builder(access = AccessLevel.PRIVATE)
    public Cancellation(Tutee tutee, Lecture lecture, LecturePrice lecturePrice, LocalDateTime enrolledAt) {
        this.tutee = tutee;
        this.lecture = lecture;
        this.lecturePrice = lecturePrice;
        this.enrolledAt = enrolledAt;
    }

    public static Cancellation of(Tutee tutee, Lecture lecture, LecturePrice lecturePrice, LocalDateTime enrolledAt) {
        return Cancellation.builder()
                .tutee(tutee)
                .lecture(lecture)
                .lecturePrice(lecturePrice)
                .enrolledAt(enrolledAt)
                .build();
    }
}
