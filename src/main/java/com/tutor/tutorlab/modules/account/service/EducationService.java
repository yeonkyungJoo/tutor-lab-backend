package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.EDUCATION;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTOR;

@Transactional
@RequiredArgsConstructor
@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final TutorRepository tutorRepository;
    // TODO - CHECK : user deleted/verified
    @Transactional(readOnly = true)
    public Education getEducation(User user, Long educationId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        return educationRepository.findByTutorAndId(tutor, educationId)
                .orElseThrow(() -> new EntityNotFoundException(EDUCATION));
    }

    public Education createEducation(User user, EducationCreateRequest educationCreateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Education education = Education.builder()
                .tutor(tutor)
                .schoolName(educationCreateRequest.getSchoolName())
                .major(educationCreateRequest.getMajor())
                .entranceDate(LocalDateTimeUtil.getStringToDate(educationCreateRequest.getEntranceDate()))
                .graduationDate(LocalDateTimeUtil.getStringToDate(educationCreateRequest.getGraduationDate()))
                .score(educationCreateRequest.getScore())
                .degree(educationCreateRequest.getDegree())
                .build();
        tutor.addEducation(education);
        return educationRepository.save(education);
    }

    public void updateEducation(User user, Long educationId, EducationUpdateRequest educationUpdateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Education education = educationRepository.findByTutorAndId(tutor, educationId)
                .orElseThrow(() -> new EntityNotFoundException(EDUCATION));

        education.setSchoolName(educationUpdateRequest.getSchoolName());
        education.setMajor(educationUpdateRequest.getMajor());
        education.setEntranceDate(LocalDateTimeUtil.getStringToDate(educationUpdateRequest.getEntranceDate()));
        education.setGraduationDate(LocalDateTimeUtil.getStringToDate(educationUpdateRequest.getGraduationDate()));
        education.setScore(educationUpdateRequest.getScore());
        education.setDegree(educationUpdateRequest.getDegree());
    }

    public void deleteEducation(User user, Long educationId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Education education = educationRepository.findByTutorAndId(tutor, educationId)
                .orElseThrow(() -> new EntityNotFoundException(EDUCATION));

        education.delete();
        // TODO - CHECK
        educationRepository.delete(education);
    }

}
