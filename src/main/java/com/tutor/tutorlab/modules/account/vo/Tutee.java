package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AttributeOverride(name = "id", column = @Column(name = "tutee_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
public class Tutee extends BaseEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String subjects;      // 학습하고 싶은 과목

    public List<String> getSubjectList() {
        if (this.subjects.length() > 0) {
            return Arrays.asList(this.subjects.split(","));
        }
        return Collections.emptyList();
    }

    public void quit() {

        setUser(null);
    }
}
