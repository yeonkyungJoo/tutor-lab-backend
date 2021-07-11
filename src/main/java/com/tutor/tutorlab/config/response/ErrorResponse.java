package com.tutor.tutorlab.config.response;

import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {
    private int code;
    private String message;
    private List<String> errorDetails;
    private String responseTime;

    public ErrorResponse(int code, String message, List<String> errorDetails) {
        this.code = code;
        this.message = message;
        this.errorDetails = errorDetails;
        this.responseTime = LocalDateTimeUtil.getNowToString("yyyy-MM-dd hh:mm:ss");
    }

}
