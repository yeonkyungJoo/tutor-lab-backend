package com.tutor.tutorlab.modules.upload.mapstruct;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UploadMapstruct {

//    @Mappings({
//            @Mapping(target = "result", source = "fileResponse"),
//            @Mapping(target = "url", source = "url")
//    })
//    UploadResponse fileToUploadResponse(FileResponse fileResponse, String url);
}
