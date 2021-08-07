package com.tutor.tutorlab.modules.subject;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfig.class)
public interface SubjectMapstruct {
    @Mappings({})
    SubjectResponse subjectToSubjectResponse(Subject subject);
}
