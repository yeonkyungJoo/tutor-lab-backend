package com.tutor.tutorlab.config.converter.enumconverter;

import com.tutor.tutorlab.modules.file.enums.FileType;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class FileTypeConverter implements AttributeConverter<FileType, String> {
    @Override
    public String convertToDatabaseColumn(FileType attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getType();
    }

    @Override
    public FileType convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return null;
        }
        return FileType.find(dbData);
    }
}
