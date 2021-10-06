package com.tutor.tutorlab.modules.purchase.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Where(clause = "closed = false and canceled = false")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "enrollment_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Enrollment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id",
                referencedColumnName = "tutee_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_ENROLLMENT_TUTEE_ID"))
    private Tutee tutee;

    // TODO - CHECK : lecture 삭제 시
    // 단방향 -> 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "lecture_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_ENROLLMENT_LECTURE_ID"))
    private Lecture lecture;

    // 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_price_id",
            referencedColumnName = "lecture_price_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_ENROLLMENT_LECTURE_PRICE_ID"))
    private LecturePrice lecturePrice;

    private boolean closed = false;

    private boolean canceled = false;

    @OneToOne(mappedBy = "enrollment", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Chatroom chatroom;

    @OneToOne(mappedBy = "enrollment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    @Builder(access = AccessLevel.PRIVATE)
    public Enrollment(Tutee tutee, Lecture lecture, LecturePrice lecturePrice) {
        this.tutee = tutee;
        this.lecture = lecture;
        this.lecturePrice = lecturePrice;
    }

    public static Enrollment of(Tutee tutee, Lecture lecture, LecturePrice lecturePrice) {
        return Enrollment.builder()
                .tutee(tutee)
                .lecture(lecture)
                .lecturePrice(lecturePrice)
                .build();
    }

    public void close() {
        setClosed(true);
    }

    public void cancel() {
        setCanceled(true);
    }

//    public void delete() {
//        this.tutee.getEnrollments().remove(this);
//        this.lecture.getEnrollments().remove(this);
//        this.chatroom = null;
//        this.review = null;
//    }
}
