package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ToString
@AttributeOverride(name = "id", column = @Column(name = "tutor_id"))
@Getter @Setter
@NoArgsConstructor
@Entity
public class Tutor extends BaseEntity {

    // TODO - CHECK : 페치 조인
    // @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @NotNull
    @JoinColumn(name = "user_id",
                referencedColumnName = "user_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_TUTOR_USER_ID"))
    private User user;
    private String subjects;

    @ToString.Exclude
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    private boolean specialist;

    @ToString.Exclude
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chatroom> chatrooms = new ArrayList<>();

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

    public void quit() {
        this.getCareers().clear();
        this.getEducations().clear();

        user.setRole(RoleType.TUTEE);
    }

    @Builder
    public Tutor(@NotNull User user, String subjects, boolean specialist) {
        this.user = user;
        this.subjects = subjects;
        this.specialist = specialist;
    }

}
