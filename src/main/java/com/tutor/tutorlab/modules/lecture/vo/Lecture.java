package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "lecture_id"))
@Entity
@Table(name = "lecture")
public class Lecture extends BaseEntity {

    // 단방향
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id",
                referencedColumnName = "tutor_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_LECTURE_TUTOR_ID"))
    private Tutor tutor;

    @Column(nullable = false, length = 40)
    private String title;

    @Column(nullable = false, length = 25)
    private String subTitle;

    @Column(nullable = false, length = 25)
    private String introduce;

    @Lob
    @Column(nullable = false, length = 25)
    private String content;

    @Column(nullable = false, length = 20)
    private DifficultyType difficultyType;

    // TODO - CHECK : prohannah.tistory.com/133
    @ElementCollection(targetClass = SystemType.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "lecture_system_type",
            joinColumns = @JoinColumn(name = "lecture_id",
                    nullable = false,
                    referencedColumnName = "lecture_id",
                    foreignKey = @ForeignKey(name = "FK_LECTURE_SYSTEM_TYPE_LECTURE_ID"))
    )   // cascade = CascadeType.ALL
    private List<SystemType> systemTypes = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LecturePrice> lecturePrices = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureSubject> lectureSubjects = new ArrayList<>();

    private String thumbnail;

    public void addSubject(LectureSubject lectureSubject) {
        lectureSubjects.add(lectureSubject);
        lectureSubject.mappingLecture(this);
    }

    public void addPrice(LecturePrice lecturePrice) {
        lecturePrices.add(lecturePrice);
        lecturePrice.mappingLecture(this);
    };

    public void addEnrollment(Enrollment enrollment) {
        enrollment.setLecture(this);
    }

    @Builder(access = PRIVATE)
    public Lecture(Tutor tutor, String title, String subTitle, String introduce, String content, DifficultyType difficultyType, List<SystemType> systemTypes, String thumbnail) {
        this.tutor = tutor;
        this.title = title;
        this.subTitle = subTitle;
        this.introduce = introduce;
        this.content = content;
        this.difficultyType = difficultyType;
        this.systemTypes = systemTypes;
        this.thumbnail = thumbnail;

        this.lecturePrices = new ArrayList<>();
        this.lectureSubjects = new ArrayList<>();
    }

    public static Lecture of(Tutor tutor, String title, String subTitle, String introduce, String content, DifficultyType difficultyType, List<SystemType> systemTypes, String thumbnail) {
        return Lecture.builder()
                .tutor(tutor)
                .title(title)
                .subTitle(subTitle)
                .introduce(introduce)
                .content(content)
                .difficultyType(difficultyType)
                .systemTypes(systemTypes)
                .thumbnail(thumbnail)
                .build();
    }

}
