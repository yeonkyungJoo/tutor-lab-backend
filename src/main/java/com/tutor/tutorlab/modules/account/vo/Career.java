package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "career_id"))
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Career extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id",
            referencedColumnName = "tutor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CAREER_TUTOR_ID"))
    private Tutor tutor;

    // 직업, 직장명, 그 외 경력, 자격증
    private String job;
    private String companyName;
    private String others;
    private String license;

    @Builder(access = AccessLevel.PRIVATE)
    private Career(Tutor tutor, String job, String companyName, String others, String license) {
        this.tutor = tutor;
        this.job = job;
        this.companyName = companyName;
        this.others = others;
        this.license = license;
    }

    public static Career of(Tutor tutor, String job, String companyName, String others, String license) {
        return Career.builder()
                .tutor(tutor)
                .job(job)
                .companyName(companyName)
                .others(others)
                .license(license)
                .build();
    }

    public void delete() {

        if (this.tutor != null) {
            this.tutor.getCareers().remove(this);
            this.tutor = null;
        }
    }

}
