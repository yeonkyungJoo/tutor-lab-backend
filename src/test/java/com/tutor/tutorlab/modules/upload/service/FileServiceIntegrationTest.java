package com.tutor.tutorlab.modules.upload.service;

import com.tutor.tutorlab.modules.upload.controller.response.FileResponse;
import com.tutor.tutorlab.modules.upload.enums.FileType;
import com.tutor.tutorlab.modules.upload.service.request.FileRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class FileServiceIntegrationTest {

    @Autowired
    private FileService fileService;

    private final String uuid = UUID.randomUUID().toString();

    // TODO
//    @Test
//    void 파일등록() {
//        FileRequest fileRequest = FileRequest.of(uuid, "test.jpg", "image/jpg", FileType.LECTURE_IMAGE, 2424L);
//        FileResponse createdFile = fileService.createFile(fileRequest);
//        assertEquals(fileService.getFile(uuid), createdFile);
//    }
}