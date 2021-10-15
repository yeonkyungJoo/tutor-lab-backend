package com.tutor.tutorlab.modules.upload.service;

import com.tutor.tutorlab.config.AmazonS3Properties;
import com.tutor.tutorlab.modules.upload.amazon.service.AWSS3Client;
import com.tutor.tutorlab.modules.upload.controller.response.FileResponse;
import com.tutor.tutorlab.modules.upload.controller.response.UploadResponse;
import com.tutor.tutorlab.modules.upload.enums.FileType;
import com.tutor.tutorlab.modules.upload.service.request.FileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public UploadResponse uploadImage(String dir, MultipartFile file) {

        try {

            String uuid = UUID.randomUUID().toString();

            String key = uuid;
            if (!StringUtils.isBlank(dir)) {
                key = dir + "/" + uuid;
            }
            awss3Client.putObject(amazonS3Properties.getBucket(), key, file.getBytes(), file.getContentType());

            FileRequest fileRequest = FileRequest.of(uuid, file.getOriginalFilename(), file.getContentType(), FileType.LECTURE_IMAGE, file.getSize());
            FileResponse fileResponse = fileService.createFile(fileRequest);
            return new UploadResponse(fileResponse, amazonS3Properties.getS3UploadUrl(key));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
