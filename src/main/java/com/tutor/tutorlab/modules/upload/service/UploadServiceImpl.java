package com.tutor.tutorlab.modules.upload.service;

import com.tutor.tutorlab.config.externalproperties.amazon.AmazonS3Properties;
import com.tutor.tutorlab.modules.external.amazon.service.AWSS3Client;
import com.tutor.tutorlab.modules.file.enums.FileType;
import com.tutor.tutorlab.modules.file.mapstruct.FileMapstruct;
import com.tutor.tutorlab.modules.file.response.FileResponse;
import com.tutor.tutorlab.modules.file.service.FileService;
import com.tutor.tutorlab.modules.upload.controller.response.UploadResponse;
import com.tutor.tutorlab.modules.upload.mapstruct.UploadMapstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {

    private final AmazonS3Properties amazonS3Properties;
    private final AWSS3Client awss3Client;
    private final FileService fileService;
    private final FileMapstruct fileMapstruct;
    private final UploadMapstruct uploadMapstruct;

    @Transactional
    @Override
    public UploadResponse uploadImage(MultipartFile file) {

        String uuid = UUID.randomUUID().toString();

        try {
            awss3Client.putObject(amazonS3Properties.getBucket(), uuid, file.getBytes(), file.getContentType());

            FileResponse fileResponse = fileService.add(fileMapstruct.toAddFile(uuid, file.getOriginalFilename(), file.getContentType(), file.getSize(), FileType.LECTURE_IMAGE));
            return uploadMapstruct.fileToUploadResponse(fileResponse, amazonS3Properties.getS3UploadUrl(fileResponse.getUuid()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
