package com.tutor.tutorlab.modules.upload.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.modules.upload.controller.request.UploadImageRequest;
import com.tutor.tutorlab.modules.upload.controller.response.FileResponse;
import com.tutor.tutorlab.modules.upload.controller.response.UploadResponse;
import com.tutor.tutorlab.modules.upload.service.UploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UploadControllerTest {

    private final String BASE_URL = "/uploads";

    @InjectMocks
    UploadController uploadController;
    @Mock
    UploadService uploadService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(uploadController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @Test
    void uploadImage() throws Exception {

        // given
        UploadResponse response = new UploadResponse(Mockito.mock(FileResponse.class), "url");
        doReturn(response)
                .when(uploadService).uploadImage("user", any(MultipartFile.class));
        // when
        // then
        UploadImageRequest request = UploadImageRequest.of(
                new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, FileCopyUtils.copyToByteArray(new ClassPathResource("image/test.png").getInputStream()))
        );
        mockMvc.perform(get(BASE_URL + "/images"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }



}
