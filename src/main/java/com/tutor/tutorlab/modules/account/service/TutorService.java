package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;

    public Tutor createTutor(User user, TutorSignUpRequest tutorSignUpRequest) {

        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects(tutorSignUpRequest.getSubjects())
                .specialist(tutorSignUpRequest.isSpecialist())
                .build();

        tutorRepository.save(tutor);
        user.setRole(RoleType.ROLE_TUTOR);

        tutorSignUpRequest.getCareers().stream().forEach(careerCreateRequest -> {
            Career career = Career.builder()
                    .tutor(tutor)
                    .companyName(careerCreateRequest.getCompanyName())
                    .duty(careerCreateRequest.getDuty())
                    .startDate(LocalDate.parse(careerCreateRequest.getStartDate()))
                    .endDate(LocalDate.parse(careerCreateRequest.getEndDate()))
                    .present(careerCreateRequest.isPresent())
                    .build();

            careerRepository.save(career);
            tutor.addCareer(career);
        });

        tutorSignUpRequest.getEducations().stream().forEach(educationCreateRequest -> {
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
        });

        return tutor;
    }

    public void updateTutor(User user, TutorUpdateRequest tutorUpdateRequest) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {

        }

        tutor.setSubjects(tutorUpdateRequest.getSubjects());
        tutor.setSpecialist(tutorUpdateRequest.isSpecialist());

        tutor.setUpdatedAt(LocalDateTime.now());
    }

    // TODO - check : CASCADE
    public void deleteTutor(User user) {

        if (user.getRole() != RoleType.ROLE_TUTOR) {
            // TODO - 에러
        }

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            // TODO - 에러
        }

        // Career 삭제
        tutor.getCareers().stream()
                .forEach(career -> {
                    career.setTutor(null);
                    careerRepository.delete(career);
                });
        // Education 삭제
        tutor.getEducations().stream()
                .forEach(education -> {
                    education.setTutor(null);
                    educationRepository.delete(education);
                });

        tutor.quit();
        tutorRepository.delete(tutor);

    }
}
