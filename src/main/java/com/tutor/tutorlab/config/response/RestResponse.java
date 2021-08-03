package com.tutor.tutorlab.config.response;

import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RestResponse {
    private int code;
    private String message;
    private Object result;
    private String responseTime;

    public static RestResponse of(int code, String message, Object result) {
        return new RestResponse(code, message, result, LocalDateTimeUtil.getNowToString("yyyy-MM-dd hh:mm:ss"));
    }

}
