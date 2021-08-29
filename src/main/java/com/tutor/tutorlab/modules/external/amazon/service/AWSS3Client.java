package com.tutor.tutorlab.modules.external.amazon.service;

import com.tutor.tutorlab.config.exception.AmazonS3Exception;

public interface AWSS3Client {

    void putObject(String bucket, String key, byte[] bytes, String contentType) throws AmazonS3Exception;

    void deleteObject(String bucket, String key) throws AmazonS3Exception;
}
