package com.tutor.tutorlab.config.exception;

public class AmazonS3Exception extends RuntimeException {
    private static final long serialVersionUID = 395373762474915663L;

    public AmazonS3Exception() {
        super();
    }

    public AmazonS3Exception(final String message) {
        super(message);
    }

    public AmazonS3Exception(final String message, final Throwable cause) {
        super(message, cause);
    }
}
