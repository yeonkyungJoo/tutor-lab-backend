package com.tutor.tutorlab.modules.file.response;

import lombok.Value;

@Value
public class FileResponse {

    private final String uuid;

    private final String name;

    private final String contentType;

    private final String type;

    private final long size;
}
