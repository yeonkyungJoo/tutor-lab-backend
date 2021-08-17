package com.tutor.tutorlab.modules.subject.mapstruct;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import com.tutor.tutorlab.modules.subject.controller.response.SubjectResponse;
import com.tutor.tutorlab.modules.subject.vo.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(config = MapstructConfig.class)
public interface SubjectMapstruct {
    @Mappings({})
    SubjectResponse subjectToSubjectResponse(Subject subject);
}
