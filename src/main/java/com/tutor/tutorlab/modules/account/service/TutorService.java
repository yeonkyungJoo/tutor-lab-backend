package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final LectureRepository lectureRepository;

    public Tutor createTutor(User user, TutorSignUpRequest tutorSignUpRequest) {

        user.setRole(RoleType.ROLE_TUTOR);
        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects(tutorSignUpRequest.getSubjects())
                .specialist(tutorSignUpRequest.isSpecialist())
                .build();

        tutorSignUpRequest.getCareers().stream().forEach(careerCreateRequest -> {
            Career career = Career.builder()
                    .tutor(tutor)
                    .companyName(careerCreateRequest.getCompanyName())
                    .duty(careerCreateRequest.getDuty())
                    .startDate(LocalDateTimeUtil.getStringToDate(careerCreateRequest.getStartDate()))
                    .endDate(LocalDateTimeUtil.getStringToDate(careerCreateRequest.getEndDate()))
                    .present(careerCreateRequest.isPresent())
                    .build();
            tutor.addCareer(career);
        });

        tutorSignUpRequest.getEducations().stream().forEach(educationCreateRequest -> {
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
        });

        tutorRepository.save(tutor);
        return tutor;
    }

    public void updateTutor(User user, TutorUpdateRequest tutorUpdateRequest) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        tutor.setSubjects(tutorUpdateRequest.getSubjects());
        tutor.setSpecialist(tutorUpdateRequest.isSpecialist());
    }

    public void deleteTutor(User user) {

        if (user.getRole() != RoleType.ROLE_TUTOR) {
            throw new UnauthorizedException();
        }

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        tutor.quit();
        tutorRepository.delete(tutor);
    }

    public List<Lecture> getTutorLecture(User user) {
        return lectureRepository.findByTutorUser(user);
    }
}
