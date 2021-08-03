package com.tutor.tutorlab.modules.address.util;

import org.apache.commons.lang3.StringUtils;

public class AddressUtils {
    private static final String EMPTY_STRING = "";

    private AddressUtils() {}

    public static String convertAddress(String address) {
        if (StringUtils.isBlank(address)) {
            return EMPTY_STRING;
        }
        return address;
    }

}
