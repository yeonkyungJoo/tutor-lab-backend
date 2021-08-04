package com.tutor.tutorlab.config.response;

import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private int code;
    private String message;
    private List<String> errorDetails = new ArrayList<>();
    private String responseTime;

    public ErrorResponse(int code, String message, List<String> errorDetails) {
        this.code = code;
        this.message = message;
        this.errorDetails.addAll(errorDetails);
        this.responseTime = LocalDateTimeUtil.getNowToString("yyyy-MM-dd hh:mm:ss");
    }

    // TODO - CHECK : 초기화 메서드
    public ErrorResponse(ErrorCode errorCode, List<String> errorDetails) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errorDetails.addAll(errorDetails);
        this.responseTime = LocalDateTimeUtil.getNowToString("yyyy-MM-dd hh:mm:ss");
    }

    public ErrorResponse(ErrorCode errorCode, String... errorDetails) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();

        if (errorDetails.length > 0) {

            for (String errorDetail : errorDetails) {
                this.errorDetails.add(errorDetail);
            }
        }
        this.responseTime = LocalDateTimeUtil.getNowToString("yyyy-MM-dd hh:mm:ss");
    }
}
