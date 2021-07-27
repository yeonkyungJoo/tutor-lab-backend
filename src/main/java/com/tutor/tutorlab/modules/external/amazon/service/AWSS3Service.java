package com.tutor.tutorlab.modules.external.amazon.service;

import com.tutor.tutorlab.config.exception.AWSS3Exception;

public interface AWSS3Service {
    void putObject(String bucket, String key, byte[] bytes, String contentType) throws AWSS3Exception;

    void deleteObject(String bucket, String key) throws AWSS3Exception;
}
