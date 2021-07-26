package com.tutor.tutorlab.modules.base;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(nullable = false, updatable = false)
    protected Long id;

    @Column(updatable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = true)
    protected LocalDateTime updatedAt;

}
