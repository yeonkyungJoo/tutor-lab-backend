package com.tutor.tutorlab.modules.file.response;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(of = "id")
@Value  // TODO - CHECK : @Value
public class FileResponse {

    Long id;
    String uuid;
    String name;
    String contentType;
    String type;
    Long size;
}
