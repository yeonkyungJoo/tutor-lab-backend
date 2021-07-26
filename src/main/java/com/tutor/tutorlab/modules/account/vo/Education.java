package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@AttributeOverride(name = "id", column = @Column(name = "education_id"))
@Getter @Setter
@NoArgsConstructor
@Entity
public class Education extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    private String schoolName;
    private String major;
    private LocalDate entranceDate;
    private LocalDate graduationDate;
    private double score;
    private String degree;  // Bachelor, Master, Doctor

    @Builder
    public Education(Tutor tutor, String schoolName, String major, LocalDate entranceDate, LocalDate graduationDate, double score, String degree) {
        this.tutor = tutor;
        this.schoolName = schoolName;
        this.major = major;
        this.entranceDate = entranceDate;
        this.graduationDate = graduationDate;
        this.score = score;
        this.degree = degree;
    }
}
