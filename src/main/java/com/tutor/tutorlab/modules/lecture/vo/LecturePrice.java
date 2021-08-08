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

    @Column(name = "is_group", nullable = false)
    private Boolean isGroup;

    @Column(name = "group_number")
    private Integer groupNumber;

    @Column(name = "total_time", nullable = false)
    private Integer totalTime;

    @Column(name = "pertime_lecture", nullable = false)
    private Integer pertimeLecture;

    @Column(name = "pertime_cost", nullable = false)
    private Long pertimeCost;

    @Column(name = "total_cost", nullable = false)
    private Long totalCost;

    public void mappingLecture(Lecture lecture) {
        this.lecture = lecture;
    };
}
