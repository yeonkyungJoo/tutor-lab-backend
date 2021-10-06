package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "education_id"))
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Education extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id",
            referencedColumnName = "tutor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_EDUCATION_TUTOR_ID"))
    private Tutor tutor;

    private String schoolName;
    private String major;
    private LocalDate entranceDate;
    private LocalDate graduationDate;
    private double score;
    private String degree;  // Bachelor, Master, Doctor

    @Builder(access = AccessLevel.PRIVATE)
    public Education(Tutor tutor, String schoolName, String major, LocalDate entranceDate, LocalDate graduationDate, double score, String degree) {
        this.tutor = tutor;
        this.schoolName = schoolName;
        this.major = major;
        this.entranceDate = entranceDate;
        this.graduationDate = graduationDate;
        this.score = score;
        this.degree = degree;
    }

    public static Education of(Tutor tutor, String schoolName, String major, LocalDate entranceDate, LocalDate graduationDate, double score, String degree) {
        return Education.builder()
                .tutor(tutor)
                .schoolName(schoolName)
                .major(major)
                .entranceDate(entranceDate)
                .graduationDate(graduationDate)
                .score(score)
                .degree(degree)
                .build();
    }

    public void delete() {
        this.tutor.getEducations().remove(this);
    }
}
