package com.tutor.tutorlab.modules.review.vo;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "review_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Review extends BaseEntity {

    private Integer score;
    private String content;

    // 단방향
    // TODO - CHECK : 양방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_REVIEW_USER_ID"))
    private User user;  // TODO - CHECK : User or Tutee/Tutor

    // OneToOne/양방향
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id",
            referencedColumnName = "enrollment_id",
            nullable = true,
            foreignKey = @ForeignKey(name = "FK_REVIEW_ENROLLMENT_ID"))
    private Enrollment enrollment;

    // 단방향
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

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> children = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Review(Integer score, String content, User user, Enrollment enrollment, Lecture lecture, Review parent) {
        this.score = score;
        this.content = content;
        this.user = user;
        this.enrollment = enrollment;
        this.lecture = lecture;
        this.parent = parent;
    }

    public static Review of(Integer score, String content, User user, Enrollment enrollment, Lecture lecture, Review parent) {
        return Review.builder()
                .score(score)
                .content(content)
                .user(user)
                .enrollment(enrollment)
                .lecture(lecture)
                .parent(parent)
                .build();
    }

    public void addChild(Review review) {
        this.children.add(review);
        review.setParent(this);
    }

    public void delete() {
        this.children.clear();
    }
}
