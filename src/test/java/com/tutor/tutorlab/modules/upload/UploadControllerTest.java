package com.tutor.tutorlab.modules.upload;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.upload.controller.request.UploadImageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadControllerTest extends AbstractTest {
    private final String BASE_URL = "/uploads";

    @Test
    void 업로드테스트() throws Exception {
        UploadImageRequest request = UploadImageRequest.builder()
                .file(new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, FileCopyUtils.copyToByteArray(new ClassPathResource("image/test.png").getInputStream())))
                .build();

        mockMvc.perform(multipart(BASE_URL + "/images")
                .file((MockMultipartFile) request.getFile())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
