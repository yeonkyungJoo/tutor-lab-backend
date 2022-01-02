package com.tutor.tutorlab.modules.purchase.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import lombok.*;

import javax.persistence.*;

//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "pick_id"))
@Getter //@Setter
@Entity
public class Pick extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id",
                referencedColumnName = "tutee_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_PICK_TUTEE_ID"))
    private Tutee tutee;

    // 양방향
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "lecture_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_PICK_LECTURE_ID"))
    private Lecture lecture;

    @Builder(access = AccessLevel.PRIVATE)
    private Pick(Tutee tutee, Lecture lecture) {
        this.tutee = tutee;
        this.lecture = lecture;
    }

    public static Pick of(Tutee tutee, Lecture lecture) {
        return Pick.builder()
                .tutee(tutee)
                .lecture(lecture)
                .build();
    }

    public void delete() {
        this.tutee.getPicks().remove(this);
    }

    public void setTutee(Tutee tutee) {
        this.tutee = tutee;
    }
}
