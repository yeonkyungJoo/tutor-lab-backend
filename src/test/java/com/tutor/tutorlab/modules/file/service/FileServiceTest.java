package com.tutor.tutorlab.modules.file.service;

import com.tutor.tutorlab.modules.file.enums.FileType;
import com.tutor.tutorlab.modules.file.mapstruct.FileMapstruct;
import com.tutor.tutorlab.modules.file.response.FileResponse;
import com.tutor.tutorlab.modules.file.vo.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileServiceTest {
// TODO - TEST
    @Autowired
    private FileService fileService;

    @Autowired
    private FileMapstruct fileMapstruct;

    @Test
    void 파일등록() {
        FileResponse fileResponse = fileService.add(fileMapstruct.toAddFile(UUID.randomUUID().toString(), "test.jpg", "image/jpg", 2424L, FileType.LECTURE_IMAGE));
        FileResponse savedFileResponse = fileService.get(fileResponse.getUuid());
        assertEquals(fileResponse, savedFileResponse);
    }
}