package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Transactional(readOnly = false)
@RequiredArgsConstructor
@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final TutorRepository tutorRepository;

    public Education createEducation(User user, EducationCreateRequest educationCreateRequest) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {

        }

        Education education = Education.builder()
                .tutor(tutor)
                .schoolName(educationCreateRequest.getSchoolName())
                .major(educationCreateRequest.getMajor())
                .entranceDate(LocalDate.parse(educationCreateRequest.getEntranceDate()))
                .graduationDate(LocalDate.parse(educationCreateRequest.getGraduationDate()))
                .score(educationCreateRequest.getScore())
                .degree(educationCreateRequest.getDegree())
                .build();
        educationRepository.save(education);
        tutor.addEducation(education);

        return education;
    }

    public void updateEducation(Long educationId, EducationUpdateRequest educationUpdateRequest) {

        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 데이터입니다."));

        education.setSchoolName(educationUpdateRequest.getSchoolName());
        education.setMajor(educationUpdateRequest.getMajor());
        education.setEntranceDate(LocalDate.parse(educationUpdateRequest.getEntranceDate()));
        education.setGraduationDate(LocalDate.parse(educationUpdateRequest.getGraduationDate()));
        education.setScore(educationUpdateRequest.getScore());
        education.setDegree(educationUpdateRequest.getDegree());

        education.setUpdatedAt(LocalDateTime.now());
    }

    public void deleteEducation(Long educationId) {

        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 데이터입니다."));

        education.delete();
        educationRepository.delete(education);
    }
}
