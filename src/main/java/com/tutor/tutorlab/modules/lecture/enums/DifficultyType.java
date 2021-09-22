package com.tutor.tutorlab.modules.lecture.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tutor.tutorlab.config.converter.enumconverter.EnumerableConverter;
import com.tutor.tutorlab.modules.base.Enumerable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Converter;

@Getter
@AllArgsConstructor
public enum DifficultyType implements Enumerable {

    BASIC("BASIC", "입문"),
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

    @javax.persistence.Converter(autoApply = true)
    public static class Converter extends EnumerableConverter<DifficultyType> {
        public Converter() {
            super(DifficultyType.class);
        }
    }
}
