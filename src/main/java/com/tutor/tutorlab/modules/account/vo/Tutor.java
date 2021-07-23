package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.account.career.Career;
import com.tutor.tutorlab.modules.account.education.Education;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AttributeOverride(name = "id", column = @Column(name = "tutor_id"))
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tutor extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;
    private String subject;

    // TODO - CASCADE
    // TODO - check : FETCH
    @OneToMany(mappedBy = "tutor")
    private List<Career> careers = new ArrayList<>();
    @OneToMany(mappedBy = "tutor")
    private List<Education> educations = new ArrayList<>();

    private boolean specialist;

    public List<String> getSubjectList() {
        if (this.subject.length() > 0) {
            return Arrays.asList(this.subject.split(","));
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

        setUser(null);
    }


}
