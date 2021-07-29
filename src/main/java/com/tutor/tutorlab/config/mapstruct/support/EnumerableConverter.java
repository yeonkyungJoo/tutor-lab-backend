package com.tutor.tutorlab.config.mapstruct.support;

import com.tutor.tutorlab.modules.base.Enumerable;
import org.mapstruct.TargetType;

import java.util.Objects;

/**
 * Mapstruct 구현체에서 사용할 Enum Converter
 */
public class EnumerableConverter {
    public static final String convert(Enumerable source) {
        return Objects.nonNull(source) ? source.getType() : null;
    }

    public static final <E extends Enum<? extends Enumerable>> E convert(String source, @TargetType Class<E> clazz) {
        return (E) Enumerable.findToNull(source, (Enumerable[]) clazz.getEnumConstants());
    }
}
