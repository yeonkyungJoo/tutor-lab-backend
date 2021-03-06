package com.tutor.tutorlab.modules.upload.controller.request;

import com.tutor.tutorlab.config.validation.Extension;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UploadImageRequest {

    @Extension(message = "png, jpg, jpeg, gif, pdf 확장자 파일만 선택해주세요.", allows = {"png", "jpg", "jpeg", "gif", "pdf"})
    @NotNull(message = "파일을 선택해주세요.")
    private MultipartFile file;

    @Builder(access = AccessLevel.PRIVATE)
    private UploadImageRequest(@NotNull(message = "파일을 선택해주세요.") MultipartFile file) {
        this.file = file;
    }

    public static UploadImageRequest of(MultipartFile file) {
        return UploadImageRequest.builder()
                .file(file)
                .build();
    }
}