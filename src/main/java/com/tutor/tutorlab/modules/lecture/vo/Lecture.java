package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "lecture")
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id",
                referencedColumnName = "id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_LECTURE_USER_ID"))
    private User user;

    @Column(name = "title", nullable = false, length = 40)
    private String title;

    @Column(name = "sub_title", nullable = false, length = 25)
    private String subTitle;

    @Column(name = "content", nullable = false, length = 25)
    private String content;

    @Column(name = "total_time", nullable = false)
    private Integer totalTime;

    @Column(name = "pertime_cost", nullable = false)
    private Long pertimeCost;

    @Column(name = "total_cost", nullable = false)
    private Long totalCost;

    @Column(name = "difficulty", length = 20)
    private String difficulty;

    @Column(name = "group_yn", length = 5)
    private String groupYn;

    @Column(name = "group_number")
    private Integer groupNumber;

    @Column(name = "system", length = 20)
    private String system;
}
