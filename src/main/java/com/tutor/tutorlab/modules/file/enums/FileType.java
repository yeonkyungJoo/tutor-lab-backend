package com.tutor.tutorlab.modules.file.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tutor.tutorlab.config.converter.enumconverter.EnumerableConverter;
import com.tutor.tutorlab.modules.base.Enumerable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Converter;

@Getter
@AllArgsConstructor
public enum FileType implements Enumerable {
    LECTURE_IMAGE("LECTURE_IMAGE", "강의 이미지");

    private String type;
    private String name;

    public static FileType find(String type) {
        return Enumerable.find(type, values());
    }

    @JsonCreator
    public static FileType findToNull(String type) {
        return Enumerable.findToNull(type, values());
    }

    @javax.persistence.Converter(autoApply = true)
    public static class Converter extends EnumerableConverter<FileType> {
        public Converter() {
            super(FileType.class);
        }
    }
}
