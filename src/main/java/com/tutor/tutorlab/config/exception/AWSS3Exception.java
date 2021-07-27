package com.tutor.tutorlab.config.exception;

public class AWSS3Exception extends RuntimeException {
    private static final long serialVersionUID = 395373762474915663L;

    public AWSS3Exception() {
        super();
    }

    public AWSS3Exception(final String message) {
        super(message);
    }

    public AWSS3Exception(final String message, final Throwable cause) {
        super(message, cause);
    }
}
