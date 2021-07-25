package com.tutor.tutorlab.modules.lecture.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tutor.tutorlab.modules.base.Enumerable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DifficultyType implements Enumerable{
    BEGINNER("BEGINNER", "초급"),
    INTERMEDIATE("INTERMEDIATE", "중급"),
    ADVANCED("ADVANCED", "고급");

    private String type;
    private String name;

    public static DifficultyType find(String type) {
        return Enumerable.find(type, values());
    }

    @JsonCreator
    public static DifficultyType findToNull(String type) {
        return Enumerable.findToNull(type, values());
    }
}
