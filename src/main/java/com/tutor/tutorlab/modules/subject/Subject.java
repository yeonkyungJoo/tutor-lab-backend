package com.tutor.tutorlab.modules.subject;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

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
