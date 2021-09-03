package com.tutor.tutorlab.modules.upload.service.dto;

import com.tutor.tutorlab.modules.file.enums.FileType;
import lombok.Value;

@Value
public class AddFile {

    private final String uuid;

    private final String name;

    private final String contentType;

    private final FileType type;

    private final long size;
}
