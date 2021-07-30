package com.tutor.tutorlab.modules.file.mapstruct;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import com.tutor.tutorlab.modules.file.enums.FileType;
import com.tutor.tutorlab.modules.file.response.FileResponse;
import com.tutor.tutorlab.modules.file.vo.File;
import com.tutor.tutorlab.modules.upload.service.dto.AddFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfig.class, imports = {FileType.class})
public interface FileMapstruct {

    @Mappings({
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "contentType", source = "contentType"),
            @Mapping(target = "size", source = "size"),
            @Mapping(target = "type", source = "type")
    })
    AddFile toAddFile(String uuid, String name, String contentType, long size, FileType type);

    @Mappings({
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "contentType", source = "contentType"),
            @Mapping(target = "size", source = "size"),
            @Mapping(target = "type", source = "type")
    })
    File addFileToFile(AddFile addFile);

    @Mappings({
            @Mapping(target = "type", expression = "java(file.getType().getType())")
    })
    FileResponse fileToFileResponse(File file);
}
