package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@AttributeOverride(name = "id", column = @Column(name = "enrollment_id"))
@NoArgsConstructor
@Getter @Setter
@Entity
public class Enrollment extends BaseEntity {

    // TODO - CHECK : 양방향으로 하는 게 좋은가?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id",
                referencedColumnName = "tutee_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_ENROLLMENT_TUTEE_ID"))
    private Tutee tutee;

    // 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "lecture_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_ENROLLMENT_LECTURE_ID"))
    private Lecture lecture;

    @Builder
    public Enrollment(Tutee tutee, Lecture lecture) {
        this.tutee = tutee;
        this.lecture = lecture;
    }
}
