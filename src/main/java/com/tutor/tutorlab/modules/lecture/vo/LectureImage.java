package com.tutor.tutorlab.modules.lecture.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "lecture_image")
public class LectureImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id",
                referencedColumnName = "id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_LECTURE_IMAGE_LECTURE_ID"))
    private Lecture lecture;

    @Column(name = "real_name", nullable = false)
    private String realName;

    @Column(name = "fake_name", nullable = false)
    private String fakeName;

    @Column(name = "size")
    private Integer size;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "extend")
    private String extend;

    @Column(name = "gu_bun")
    private Integer guBun;

    class Test {

    }

}
