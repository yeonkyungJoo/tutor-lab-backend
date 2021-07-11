package com.tutor.tutorlab.config.response;

import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RestResponse<T> {
    private int code;
    private String message;
    private T result;
    private String responseTime;

    public RestResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.responseTime = LocalDateTimeUtil.getNowToString("yyyy-MM-dd hh:mm:ss");
    }

}
