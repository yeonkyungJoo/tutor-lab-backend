package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "lecture_price_id"))
@Entity
@Table(name = "lecture_price")
public class LecturePrice extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "lecture_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_LECTURE_PRICE_LECTURE_ID"))
    private Lecture lecture;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isGroup;

    private Integer groupNumber;

    @Column(nullable = false)
    private Integer totalTime;

    @Column(nullable = false)
    private Integer pertimeLecture;

    @Column(nullable = false)
    private Long pertimeCost;

    @Column(nullable = false)
    private Long totalCost;

    public void mappingLecture(Lecture lecture) {
        this.lecture = lecture;
    };
}
