package com.tutor.tutorlab.modules.lecture.enums;

import com.tutor.tutorlab.modules.base.Enumerable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DifficultyType implements Enumerable {
    BEGINNER("BEGINNER", "초급"),
    INTERMEDIATE("INTERMEDIATE", "중급"),
    ADVANCED("ADVANCED", "고급");

    private String type;
    private String name;
}
