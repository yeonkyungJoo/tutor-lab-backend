package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.EducationLevelType;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "education_id"))
@Getter
//@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Education extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id",
            referencedColumnName = "tutor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_EDUCATION_TUTOR_ID"))
    private Tutor tutor;

    // 최종학력, 학교명, 전공명, 그 외 학력
    @Enumerated(EnumType.STRING)
    private EducationLevelType educationLevel;
    private String schoolName;
    private String major;
    private String others;

    @Builder(access = AccessLevel.PRIVATE)
    private Education(Tutor tutor, EducationLevelType educationLevel, String schoolName, String major, String others) {
        this.tutor = tutor;
        this.educationLevel = educationLevel;
        this.schoolName = schoolName;
        this.major = major;
        this.others = others;
    }

    public static Education of(Tutor tutor, EducationLevelType educationLevel, String schoolName, String major, String others) {
        return Education.builder()
                .tutor(tutor)
                .educationLevel(educationLevel)
                .schoolName(schoolName)
                .major(major)
                .others(others)
                .build();
    }

    public void update(EducationUpdateRequest educationUpdateRequest) {
        this.educationLevel = educationUpdateRequest.getEducationLevel();
        this.schoolName = educationUpdateRequest.getSchoolName();
        this.major = educationUpdateRequest.getMajor();
        this.others = educationUpdateRequest.getOthers();
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public void delete() {
        if (this.tutor != null) {
            this.tutor.getEducations().remove(this);
            this.tutor = null;
        }
    }
}
