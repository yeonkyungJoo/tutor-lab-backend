package com.tutor.tutorlab.modules.upload.service;

import com.tutor.tutorlab.modules.upload.controller.response.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    UploadResponse uploadImage(String dir, MultipartFile file);
}
