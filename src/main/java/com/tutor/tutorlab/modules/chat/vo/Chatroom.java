package com.tutor.tutorlab.modules.chat.vo;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.*;

import javax.persistence.*;

@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@AttributeOverride(name = "id", column = @Column(name = "chatroom_id"))
@Entity
public class Chatroom extends BaseEntity {

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id",
            referencedColumnName = "enrollment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHATROOM_ENROLLMENT_ID"))
    private Enrollment enrollment;

    // TODO - CHECK : 페치 조인
    // @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id",
            referencedColumnName = "tutor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHATROOM_TUTOR_ID"))
    private Tutor tutor;

    // TODO - CHECK : 페치 조인
    // @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id",
            referencedColumnName = "tutee_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHATROOM_TUTEE_ID"))
    private Tutee tutee;

    @Builder(access = AccessLevel.PRIVATE)
    public Chatroom(Enrollment enrollment, Tutor tutor, Tutee tutee) {
        this.enrollment = enrollment;
        this.tutor = tutor;
        this.tutee = tutee;
    }

    public static Chatroom of(Enrollment enrollment, Tutor tutor, Tutee tutee) {
        return Chatroom.builder()
                .enrollment(enrollment)
                .tutor(tutor)
                .tutee(tutee)
                .build();
    }

    public void delete() {
        this.enrollment.setChatroom(null);
        this.tutor.getChatrooms().remove(this);
        this.tutee.getChatrooms().remove(this);
    }

}
