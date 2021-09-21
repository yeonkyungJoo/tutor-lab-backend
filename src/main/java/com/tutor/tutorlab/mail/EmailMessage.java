package com.tutor.tutorlab.mail;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailMessage {

    private String to;
    // private String from;
    private String subject;   // title
    private String content;

}
