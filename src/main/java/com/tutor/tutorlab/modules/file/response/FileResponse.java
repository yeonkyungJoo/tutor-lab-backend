package com.tutor.tutorlab.modules.file.response;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(of = "id")
@Value
public class FileResponse {

    private final long id;

    private final String uuid;

    private final String name;

    private final String contentType;

    private final String type;

    private final long size;
}
