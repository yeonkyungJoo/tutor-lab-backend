package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "lecture_id"))
@Entity
@Table(name = "lecture")
public class Lecture extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tutor_id",
                referencedColumnName = "tutor_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_LECTURE_TUTOR_ID"))
    private Tutor tutor;

    @Column(name = "title", nullable = false, length = 40)
    private String title;

    @Column(name = "sub_title", nullable = false, length = 25)
    private String subTitle;

    @Column(name = "introduce", nullable = false, length = 25)
    private String introduce;

    @Column(name = "content", nullable = false, length = 25)
    private String content;

    @Column(name = "difficulty_type", nullable = false, length = 20)
    private DifficultyType difficultyType;

    @ElementCollection
    @Column(name = "system_types", nullable = false, length = 20)
    private List<SystemType> systemTypes;

    @OneToMany(mappedBy = "lecture", cascade = ALL, orphanRemoval = true)
    private List<LecturePrice> lecturePrices;

    @OneToMany(mappedBy = "lecture", cascade = ALL, orphanRemoval = true)
    private List<LectureSubject> lectureSubjects;

    private String thumbnail;

    public void addSubject(LectureSubject lectureSubject) {
        lectureSubjects.add(lectureSubject);
        lectureSubject.mappingLecture(this);
    }

    public void removeSubject(LectureSubject lectureSubject) {
        lectureSubjects.remove(lectureSubject);
        lectureSubject.mappingLecture(null);
    }

    public void addPrice(LecturePrice lecturePrice) {
        lecturePrices.add(lecturePrice);
        lecturePrice.mappingLecture(this);
    };

    public void removePrice(LecturePrice lecturePrice) {
        lecturePrices.remove(lecturePrice);
        lecturePrice.mappingLecture(null);
    }

}
