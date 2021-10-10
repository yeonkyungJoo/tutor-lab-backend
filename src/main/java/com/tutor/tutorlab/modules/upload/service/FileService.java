package com.tutor.tutorlab.modules.upload.service;

import com.tutor.tutorlab.modules.upload.controller.response.FileResponse;
import com.tutor.tutorlab.modules.upload.service.request.FileRequest;

public interface FileService {

    FileResponse createFile(FileRequest fileRequest);
    FileResponse getFile(String uuid);
}
