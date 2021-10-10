package com.tutor.tutorlab.modules.subject.vo;

import com.tutor.tutorlab.modules.lecture.embeddable.LearningKind;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
@Table(name = "subject")
@Entity
public class Subject {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

//    @Column(length = 50, nullable = false)
//    private Long learningKindId;
//    @Column(length = 50, nullable = false)
//    private String learningKind;
    @Embedded
    private LearningKind learningKind;

    @Column(length = 50, nullable = false)
    private String krSubject;

    @Builder(access = PRIVATE)
    public Subject(LearningKind learningKind, String krSubject) {
        this.learningKind = learningKind;
        this.krSubject = krSubject;
    }

    public static Subject of(LearningKind learningKind, String krSubject) {
        return Subject.builder()
                .learningKind(learningKind)
                .krSubject(krSubject)
                .build();
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", learningKind=" + learningKind +
                ", krSubject='" + krSubject + '\'' +
                '}';
    }
}
