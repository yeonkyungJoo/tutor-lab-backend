package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AttributeOverride(name = "id", column = @Column(name = "tutor_id"))
@Getter @Setter
@NoArgsConstructor
@Entity
public class Tutor extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;
    private String subjects;

    // TODO - CASCADE
    // TODO - check : FETCH
    @OneToMany(mappedBy = "tutor")
    private List<Career> careers = new ArrayList<>();
    @OneToMany(mappedBy = "tutor")
    private List<Education> educations = new ArrayList<>();

    private boolean specialist;

    public List<String> getSubjectList() {
        if (this.subjects.length() > 0) {
            return Arrays.asList(this.subjects.split(","));
        }
        return Collections.emptyList();
    }

    public void addCareer(Career career) {
        career.setTutor(this);
        this.careers.add(career);
    }

    public void addEducation(Education education) {
        education.setTutor(this);
        this.educations.add(education);
    }

    // TODO - CHECK
    public void quit() {
        this.careers.stream()
                .forEach(career -> career.setTutor(null));
        this.careers.clear();

        this.educations.stream()
                .forEach(education -> education.setTutor(null));
        this.educations.clear();

        // setUser(null);
        // TODO - CHECK
        this.user.setRole(RoleType.ROLE_TUTEE);
    }

    @Builder
    public Tutor(@NotNull User user, String subjects, boolean specialist) {

        this.user = user;
        this.subjects = subjects;
        this.specialist = specialist;
    }
}
