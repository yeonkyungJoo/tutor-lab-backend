package com.tutor.tutorlab.modules.upload.amazon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tutor.tutorlab.config.exception.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class AWSS3ClientImpl implements AWSS3Client {

//    private final AmazonS3 amazonS3;
//
//    @Override
//    public void putObject(String bucket, String key, byte[] bytes, String contentType) throws AmazonS3Exception {
//
//        try (InputStream is = new ByteArrayInputStream(bytes)) {
//
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentType(contentType);
//            objectMetadata.setContentLength(bytes.length);
//
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, is, objectMetadata);
//            amazonS3.putObject(putObjectRequest);
//
//        } catch (Exception e) {
//            // TODO - 로그 포맷
//            log.error(ExceptionUtils.getMessage(e));
//            throw new AmazonS3Exception(AmazonS3Exception.UPLOAD, e);
//        }
//    }
//
//    @Override
//    public void deleteObject(String bucket, String key) throws AmazonS3Exception {
//
//        try {
//            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
//            amazonS3.deleteObject(deleteObjectRequest);
//        } catch (Exception e) {
//            log.error(ExceptionUtils.getMessage(e));
//            throw new AmazonS3Exception(AmazonS3Exception.DELETE, e);
//        }
//    }

}
