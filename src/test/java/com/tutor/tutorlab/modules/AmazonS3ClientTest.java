package com.tutor.tutorlab.modules;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.tutor.tutorlab.config.externalproperties.amazon.AmazonS3Properties;
import com.tutor.tutorlab.modules.external.amazon.service.AWSS3Client;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AmazonS3ClientTest {

    @Autowired
    private AWSS3Client awss3Client;

    @Autowired
    private AmazonS3Properties awss3Properties;
    
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Test
    void S3_업로드_테스트() throws Exception {
        // given
        File file = new ClassPathResource("image/test.png").getFile();
        final String uuid = UUID.randomUUID().toString();
        byte[] bytes = FileUtils.readFileToByteArray(file);
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(file.getName());

        // when
        awss3Client.putObject(awss3Properties.getBucket(), uuid, bytes, contentType);

        // then
        S3Object s3Object = amazonS3Client.getObject(awss3Properties.getBucket(), uuid);
        assertThat(s3Object).isNotNull();
        assertThat(s3Object.getBucketName()).isEqualTo(awss3Properties.getBucket());
        assertThat(s3Object.getKey()).isEqualTo(uuid);

        // uuid 확인
        System.out.println(awss3Properties.getS3UploadUrl(s3Object.getKey()));
    }

}