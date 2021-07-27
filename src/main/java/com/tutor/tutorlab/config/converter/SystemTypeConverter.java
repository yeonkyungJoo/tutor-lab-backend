package com.tutor.tutorlab.config.converter;

import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class SystemTypeConverter implements AttributeConverter<SystemType, String> {
    @Override
    public String convertToDatabaseColumn(SystemType attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getType();
    }

    @Override
    public SystemType convertToEntityAttribute(String dbData) {
        if (StringUtils.isEmpty(dbData)) {
            return null;
        }
        return SystemType.findToNull(dbData);
    }
}