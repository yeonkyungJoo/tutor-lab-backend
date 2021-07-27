package com.tutor.tutorlab.config.converter;

import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class DifficultyTypeConverter implements AttributeConverter<DifficultyType, String> {
    @Override
    public String convertToDatabaseColumn(DifficultyType attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getType();
    }

    @Override
    public DifficultyType convertToEntityAttribute(String dbData) {
        if (StringUtils.isEmpty(dbData)) {
            return null;
        }
        return DifficultyType.findToNull(dbData);
    }
}
