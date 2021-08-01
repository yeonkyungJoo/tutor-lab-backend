package com.tutor.tutorlab.config.exception;

import java.io.Serializable;

public class UploadException extends RuntimeException {

    private static final long serialVersionUID = -4795266740198306069L;

    public UploadException() {
        super();
    }

    public UploadException(final String message) {
        super(message);
    }

    public UploadException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
