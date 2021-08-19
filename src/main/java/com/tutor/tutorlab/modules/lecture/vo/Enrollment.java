package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AttributeOverride(name = "id", column = @Column(name = "enrollment_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class Enrollment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id",
                referencedColumnName = "tutee_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_ENROLLMENT_TUTEE_ID"))
    private Tutee tutee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "lecture_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_ENROLLMENT_LECTURE_ID"))
    private Lecture lecture;

}
