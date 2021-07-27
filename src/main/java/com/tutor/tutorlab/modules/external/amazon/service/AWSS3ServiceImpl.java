package com.tutor.tutorlab.modules.external.amazon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tutor.tutorlab.config.exception.AWSS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class AWSS3ServiceImpl implements AWSS3Service {

    private final AmazonS3 amazonS3;

    @Override
    public void putObject(String bucket, String key, byte[] bytes, String contentType) throws AWSS3Exception {

        try (InputStream is = new ByteArrayInputStream(bytes)) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            objectMetadata.setContentLength(bytes.length);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, is, objectMetadata);
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            log.error("S3에 파일을 업로드하는 도중에 에러가 발생하였습니다.", e);
            throw new AWSS3Exception("S3에 파일을 업로드에 실패하였습니다.", e);
        }
    }

    @Override
    public void deleteObject(String bucket, String key) throws AWSS3Exception {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("S3의 파일을 삭제하는 도중에 에러가 발생하였습니다.", e);
            throw new AWSS3Exception("S3의 파일을 삭제하는데에 실패하였습니다.", e);
        }
    }

}
