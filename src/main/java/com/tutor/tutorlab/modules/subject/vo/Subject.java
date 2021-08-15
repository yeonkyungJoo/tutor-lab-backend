package com.tutor.tutorlab.modules.subject.vo;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
@Table(name = "subject")
@Entity
public class Subject {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(length = 50, nullable = false)
    private String parent;

    @Column(length = 50, nullable = false)
    private String enSubject;

    @Column(length = 50, nullable = false)
    private String krSubject;

    @Column(length = 50, nullable = false)
    private String learningKind;
}
