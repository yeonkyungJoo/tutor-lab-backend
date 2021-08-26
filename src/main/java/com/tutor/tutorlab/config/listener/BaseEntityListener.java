package com.tutor.tutorlab.config.listener;

import com.tutor.tutorlab.modules.base.BaseEntity;

import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class BaseEntityListener {

    @PreUpdate
    public void preUpdate(Object o) {
        if (o instanceof BaseEntity) {
            ((BaseEntity) o).setUpdatedAt(LocalDateTime.now());
        }
    }
}
