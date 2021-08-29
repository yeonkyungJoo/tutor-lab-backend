package com.tutor.tutorlab.modules.file.service;

import com.tutor.tutorlab.modules.file.response.FileResponse;
import com.tutor.tutorlab.modules.file.vo.File;
import com.tutor.tutorlab.modules.upload.service.dto.AddFile;

public interface FileService {

    FileResponse add(AddFile addFile);

    FileResponse get(String uuid);
}
