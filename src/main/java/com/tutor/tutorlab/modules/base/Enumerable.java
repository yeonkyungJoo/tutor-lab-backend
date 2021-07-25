package com.tutor.tutorlab.modules.base;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/*
    TODO 1. Enumerable 타입 converter 필요.
*/
public interface Enumerable {

    String getType();

    String getName();

    static <T extends Enumerable> T find(String type, T[] values) {
        T findValue = findToNull(type, values);

        Optional.ofNullable(findValue)
                .orElseThrow(() -> new RuntimeException(String.format("지원 하지 않는 형식 입니다.(형식 : %s)", type)));

        return findValue;
    }

    static <T extends Enumerable> T findToNull(String type, T[] values) {
        if (StringUtils.isBlank(type)) {
            return null;
        }

        return Arrays.stream(values)
                .filter(value -> value.getType().equals(type))
                .findFirst()
                .orElse(null);
    }
}
