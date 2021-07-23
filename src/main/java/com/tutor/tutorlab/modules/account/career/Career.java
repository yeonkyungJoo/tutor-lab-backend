package com.tutor.tutorlab.modules.account.career;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Career extends BaseEntity {

    // TODO - LAZY or EAGER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    private String companyName;
    private String duty;    // 직급
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean present;

}
