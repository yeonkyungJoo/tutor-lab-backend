package com.tutor.tutorlab.modules.purchase.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "cancellation_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Cancellation extends BaseEntity {

    // TODO - CHECK : 양방향
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id",
            referencedColumnName = "enrollment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CANCELLATION_ENROLLMENT_ID"))
    private Enrollment enrollment;

    // private String reason;

    private Cancellation(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public static Cancellation of(Enrollment enrollment) {
        return new Cancellation(enrollment);
    }

}
