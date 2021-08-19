package com.tutor.tutorlab.config.response;

import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RestResponse<T> {
    private int code;
    private String message;
    private T result;
    private String responseTime;

    public static <T> RestResponse of(int code, String message, T result) {
        return new RestResponse(code, message, result, LocalDateTimeUtil.getNowToString("yyyy-MM-dd hh:mm:ss"));
    }

}
