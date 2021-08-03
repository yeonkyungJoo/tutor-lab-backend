package com.tutor.tutorlab.modules.upload.mapstruct;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import com.tutor.tutorlab.modules.file.response.FileResponse;
import com.tutor.tutorlab.modules.upload.controller.response.UploadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfig.class)
public interface UploadMapstruct {
    @Mappings({
            @Mapping(target = "result", source = "fileResponse"),
            @Mapping(target = "url", source = "url")
    })
    UploadResponse fileToUploadResponse(FileResponse fileResponse, String url);
}
