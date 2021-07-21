package com.tutor.tutorlab.config.response;

import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
@Getter
public class ErrorResponse {
    private int code;
    private String message;
    private List<String> errorDetails;
    private String responseTime;

    public static ErrorResponse of(int code, String message, List<String> errorDetails) {
        return new ErrorResponse(code, message, errorDetails, LocalDateTimeUtil.getNowToString("yyyy-MM-dd hh:mm:ss"));
    }

}
