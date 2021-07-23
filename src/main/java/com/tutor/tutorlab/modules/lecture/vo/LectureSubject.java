package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "lecture_subject")
public class LectureSubject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_LECTURE_SUBJECT_LECTURE_ID"))
    private Lecture lecture;

    @Column(name = "subject_kind")
    private String subjectKind;

    @Column(name = "subject_content")
    private String subjectContent;

}
