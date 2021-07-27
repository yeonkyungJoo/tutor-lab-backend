package com.tutor.tutorlab.modules.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    protected Long id;

    @Column(updatable = false)
    protected LocalDateTime createdAt;
    @Column(nullable = true)
    protected LocalDateTime updatedAt;

}
