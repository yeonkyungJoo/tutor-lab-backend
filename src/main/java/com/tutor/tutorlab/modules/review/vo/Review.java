package com.tutor.tutorlab.modules.review.vo;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@ToString
@AttributeOverride(name = "id", column = @Column(name = "review_id"))
@NoArgsConstructor
@Getter @Setter
@Entity
public class Review extends BaseEntity {

    private Integer score;
    private String content;

    // TODO - CHECK : 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_REVIEW_USER_ID"))
    private User user;  // TODO - CHECK : User or Tutee/Tutor

    // TODO - CHECK : 양방향
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id",
            referencedColumnName = "enrollment_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "FK_REVIEW_ENROLLMENT_ID"))
    private Enrollment enrollment;

    // TODO - CHECK : 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id",
            referencedColumnName = "lecture_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_REVIEW_LECTURE_ID"))
    private Lecture lecture;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",
            referencedColumnName = "review_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "FK_REVIEW_PARENT_ID"))
    private Review parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> children = new ArrayList<>();

    public void addChild(Review review) {
        this.children.add(review);
        review.setParent(this);
    }
}
