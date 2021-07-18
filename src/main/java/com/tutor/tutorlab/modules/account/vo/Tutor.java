package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Entity
public class Tutor extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;
/*
    private Set<Subject> subjects;
    private Career career;
    private Education education;
*/
    private boolean specialist;

}
