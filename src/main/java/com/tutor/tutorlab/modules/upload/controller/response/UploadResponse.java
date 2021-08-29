package com.tutor.tutorlab.modules.upload.controller.response;

import com.tutor.tutorlab.modules.file.response.FileResponse;
import lombok.Value;

@Value
public class UploadResponse {

    private final FileResponse result;

    private final String url;
}
