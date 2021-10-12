package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

    private String companyName;
    private String duty;    // 직급
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean present;

    @Builder(access = AccessLevel.PRIVATE)
    public Career(Tutor tutor, String companyName, String duty, LocalDate startDate, LocalDate endDate, boolean present) {
        this.tutor = tutor;
        this.companyName = companyName;
        this.duty = duty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.present = present;
    }

    public static Career of(Tutor tutor, String companyName, String duty, LocalDate startDate, LocalDate endDate, boolean present) {
        return Career.builder()
                .tutor(tutor)
                .companyName(companyName)
                .duty(duty)
                .startDate(startDate)
                .endDate(endDate)
                .present(present)
                .build();
    }

    public void delete() {
        this.tutor.getCareers().remove(this);
    }

}
