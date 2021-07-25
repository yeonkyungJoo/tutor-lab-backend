package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Entity
public class Tutee extends BaseEntity {

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
                referencedColumnName = "id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_TUTEE_USER_ID"))
    private User user;

    // private Set<Subject> subjects;      // 학습하고 싶은 과목

}
