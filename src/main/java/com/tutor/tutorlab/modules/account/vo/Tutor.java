package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Entity
public class Tutor extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "user_id",
                referencedColumnName = "user_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_TUTOR_USER_ID"))
    private User user;
/*
    private Set<Subject> subjects;
    private Career career;
    private Education education;
*/
    private boolean specialist;

}
