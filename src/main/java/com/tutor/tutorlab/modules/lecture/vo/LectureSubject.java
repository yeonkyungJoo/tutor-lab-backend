package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
@Getter
@AttributeOverride(name = "id", column = @Column(name = "lecture_subject_id"))
@Entity
@Table(name = "lecture_subject")
public class LectureSubject extends BaseEntity {

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

    @Builder(access = PRIVATE)
    public LectureSubject(Lecture lecture, String parent, String krSubject) {
        this.lecture = lecture;
        this.parent = parent;
        this.krSubject = krSubject;
    }

    public static LectureSubject of(Lecture lecture, String parent, String krSubject) {
        return LectureSubject.builder()
                .lecture(lecture)
                .parent(parent)
                .krSubject(krSubject)
                .build();
    }
}