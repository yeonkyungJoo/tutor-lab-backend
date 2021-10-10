package com.tutor.tutorlab.modules.upload.service;

import com.tutor.tutorlab.config.externalproperties.amazon.AmazonS3Properties;
import com.tutor.tutorlab.modules.external.amazon.service.AWSS3Client;
import com.tutor.tutorlab.modules.upload.controller.response.UploadResponse;
import com.tutor.tutorlab.modules.upload.enums.FileType;
import com.tutor.tutorlab.modules.upload.service.request.FileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {

    private final AmazonS3Properties amazonS3Properties;
    private final AWSS3Client awss3Client;
    private final FileService fileService;

    @Override
    public UploadResponse uploadImage(MultipartFile file) {

        try {

            String uuid = UUID.randomUUID().toString();
            awss3Client.putObject(amazonS3Properties.getBucket(), uuid, file.getBytes(), file.getContentType());

            FileRequest fileRequest = FileRequest.of(uuid, file.getOriginalFilename(), file.getContentType(), FileType.LECTURE_IMAGE, file.getSize());
            return new UploadResponse(fileService.createFile(fileRequest), amazonS3Properties.getS3UploadUrl(uuid));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
