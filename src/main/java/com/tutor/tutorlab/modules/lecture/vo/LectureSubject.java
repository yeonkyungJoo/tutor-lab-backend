package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Getter
@AttributeOverride(name = "id", column = @Column(name = "lecture_subject_id"))
@Entity
@Table(name = "lecture_subject")
public class LectureSubject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "lecture_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_LECTURE_SUBJECT_LECTURE_ID"))
    private Lecture lecture;

    @Column(length = 50, nullable = false)
    private String parent;

    @Column(length = 50, nullable = false)
    private String krSubject;

    public void mappingLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}