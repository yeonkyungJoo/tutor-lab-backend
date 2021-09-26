package com.tutor.tutorlab.modules.upload.service.dto;

import com.tutor.tutorlab.modules.file.enums.FileType;
import lombok.Value;

@Value
public class AddFile {

    String uuid;
    String name;
    String contentType;
    FileType type;
    long size;
}
