package com.tutor.tutorlab.modules.chat.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter @Setter
@AttributeOverride(name = "id", column = @Column(name = "chatroom_id"))
@Entity
public class Chatroom extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id",
            referencedColumnName = "enrollment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHATROOM_ENROLLMENT_ID"))
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id",
            referencedColumnName = "tutor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHATROOM_TUTOR_ID"))
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id",
            referencedColumnName = "tutee_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHATROOM_TUTEE_ID"))
    private Tutee tutee;

    @Builder
    public Chatroom(Enrollment enrollment, Tutor tutor, Tutee tutee) {
        this.enrollment = enrollment;
        this.tutor = tutor;
        this.tutee = tutee;
    }

    public void delete() {
        setTutee(null);
        setTutor(null);
    }

}
