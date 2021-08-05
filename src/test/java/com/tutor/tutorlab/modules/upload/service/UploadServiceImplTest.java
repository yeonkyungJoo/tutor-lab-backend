package com.tutor.tutorlab.modules.upload.service;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.file.enums.FileType;
import com.tutor.tutorlab.modules.upload.controller.response.UploadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;

import static org.assertj.core.api.Assertions.assertThat;

class UploadServiceImplTest extends AbstractTest {

    @Autowired
    private UploadService uploadService;

    @Test
    void 이미지업로드_테스트() throws Exception {
        String name = "test.png";
        MockMultipartFile mockFile = new MockMultipartFile("file",
                name,
                MediaType.IMAGE_PNG_VALUE,
                FileCopyUtils.copyToByteArray(new ClassPathResource("image/test.png").getInputStream()));

        UploadResponse uploadResponse = uploadService.uploadImage(mockFile);

        assertThat(uploadResponse).isNotNull();
        assertThat(uploadResponse).extracting("url").isNotNull();
        assertThat(uploadResponse).extracting("result.uuid").isNotNull();
        assertThat(uploadResponse).extracting("result.name").isEqualTo(name);
        assertThat(uploadResponse).extracting("result.size").isEqualTo(mockFile.getSize());
        assertThat(uploadResponse).extracting("result.contentType").isEqualTo(mockFile.getContentType());
        assertThat(uploadResponse).extracting("result.type").isEqualTo(FileType.LECTURE_IMAGE.getType());
    }

}