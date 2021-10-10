package com.tutor.tutorlab.modules.upload.mapstruct;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import com.tutor.tutorlab.modules.upload.enums.FileType;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class, imports = {FileType.class})
public interface FileMapstruct {

//    @Mappings({
//            @Mapping(target = "uuid", source = "uuid"),
//            @Mapping(target = "name", source = "name"),
//            @Mapping(target = "contentType", source = "contentType"),
//            @Mapping(target = "size", source = "size"),
//            @Mapping(target = "type", source = "type")
//    })
//    AddFile toAddFile(String uuid, String name, String contentType, long size, FileType type);
//
//    @Mappings({
//            @Mapping(target = "uuid", source = "uuid"),
//            @Mapping(target = "name", source = "name"),
//            @Mapping(target = "contentType", source = "contentType"),
//            @Mapping(target = "size", source = "size"),
//            @Mapping(target = "type", source = "type")
//    })
//    FileRequest addFileToFile(AddFile addFile);
//
//    @Mappings({
//            @Mapping(target = "type", expression = "java(file.getType().getType())")
//    })
//    FileResponse fileToFileResponse(FileRequest file);
}
