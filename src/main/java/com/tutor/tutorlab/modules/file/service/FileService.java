package com.tutor.tutorlab.modules.file.service;

import com.tutor.tutorlab.modules.file.vo.File;
import com.tutor.tutorlab.modules.upload.service.dto.AddFile;

public interface FileService {
    File add(AddFile addFile);
}
