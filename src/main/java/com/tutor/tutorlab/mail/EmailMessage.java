package com.tutor.tutorlab.mail;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailMessage {

    private String to;
    private String from;
    private String message;

    @Builder
    public EmailMessage(String to, String from, String message) {
        this.to = to;
        this.from = from;
        this.message = message;
    }
}
