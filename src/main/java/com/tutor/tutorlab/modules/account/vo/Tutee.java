package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import com.tutor.tutorlab.utils.CommonUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.tutor.tutorlab.utils.CommonUtil.COMMA;

//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "tutee_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Tutee extends BaseEntity {

    // TODO - CHECK : 페치 조인
    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id",
                referencedColumnName = "user_id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_TUTEE_USER_ID"))
    private User user;
    private String subjects;      // 학습하고 싶은 과목

    @OneToMany(mappedBy = "tutee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "tutee", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Pick> picks = new ArrayList<>();

    // TODO - CHECK
    @ToString.Exclude
    @OneToMany(mappedBy = "tutee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chatroom> chatrooms = new ArrayList<>();

    public List<String> getSubjectList() {
        if (this.subjects != null && this.subjects.length() > 0) {
            return Arrays.asList(this.subjects.split(COMMA));
        }
        return Collections.emptyList();
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setTutee(this);
    }

    public void addPick(Pick pick) {
        this.picks.add(pick);
        pick.setTutee(this);
    }

//    public void quit() {
//        this.user.quit();
//        setUser(null);
//    }

    private Tutee(@NotNull User user) {
        this.user = user;
    }

    public static Tutee of(User user) {
        return new Tutee(user);
    }

}
