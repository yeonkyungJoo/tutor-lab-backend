package com.tutor.tutorlab.modules.purchase.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "pick_id"))
@Getter @Setter
@Entity
public class Pick extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id",
                referencedColumnName = "tutee_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_PICK_TUTEE_ID"))
    private Tutee tutee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "lecture_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_PICK_LECTURE_ID"))
    private Lecture lecture;

    @Builder
    public Pick(Tutee tutee, Lecture lecture) {
        this.tutee = tutee;
        this.lecture = lecture;
    }
}
