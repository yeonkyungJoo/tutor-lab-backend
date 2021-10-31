package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.CareerResponse;
import com.tutor.tutorlab.modules.account.controller.response.EducationResponse;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.controller.response.TutorResponse;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.*;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentResponse;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.*;

@Transactional
@Service
@RequiredArgsConstructor
public class TutorService extends AbstractService {

    private final TutorRepository tutorRepository;

    private final UserRepository userRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;

    private final LectureService lectureService;
    private final LectureRepository lectureRepository;
    // private final LectureMapstructUtil lectureMapstructUtil;

    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    private Page<Tutor> getTutors(Integer page) {
        return tutorRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public Page<TutorResponse> getTutorResponses(Integer page) {
        return getTutors(page).map(TutorResponse::new);
    }

    private Tutor getTutor(Long tutorId) {
        return tutorRepository.findById(tutorId).orElseThrow(() -> new EntityNotFoundException(TUTOR));
    }

    public TutorResponse getTutorResponse(Long tutorId) {
        return new TutorResponse(getTutor(tutorId));
    }

    public Tutor createTutor(User user, TutorSignUpRequest tutorSignUpRequest) {

        // TODO - CHECK
        if (!user.isEmailVerified()) {
            // TODO - throw
        }
        user = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(USER));
        user.setRole(RoleType.TUTOR);

        Tutor tutor = Tutor.of(user);
        tutorSignUpRequest.getCareers().forEach(careerCreateRequest -> {
            Career career = Career.of(
                    tutor,
                    careerCreateRequest.getJob(),
                    careerCreateRequest.getCompanyName(),
                    careerCreateRequest.getOthers(),
                    careerCreateRequest.getLicense()
            );
            tutor.addCareer(career);
        });

        tutorSignUpRequest.getEducations().forEach(educationCreateRequest -> {
            Education education = Education.of(
                    tutor,
                    educationCreateRequest.getEducationLevel(),
                    educationCreateRequest.getSchoolName(),
                    educationCreateRequest.getMajor(),
                    educationCreateRequest.getOthers()
            );
            tutor.addEducation(education);
        });

        tutorRepository.save(tutor);
        return tutor;
    }

    // TODO - TEST
    public void updateTutor(User user, TutorUpdateRequest tutorUpdateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
            .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        tutor.getCareers().clear();
        tutor.getEducations().clear();

        tutorUpdateRequest.getCareers().forEach(careerUpdateRequest -> {
            Career career = Career.of(
                    tutor,
                    careerUpdateRequest.getJob(),
                    careerUpdateRequest.getCompanyName(),
                    careerUpdateRequest.getOthers(),
                    careerUpdateRequest.getLicense()
            );
            tutor.addCareer(career);
        });

        tutorUpdateRequest.getEducations().forEach(educationUpdateRequest -> {
            Education education = Education.of(
                    tutor,
                    educationUpdateRequest.getEducationLevel(),
                    educationUpdateRequest.getSchoolName(),
                    educationUpdateRequest.getMajor(),
                    educationUpdateRequest.getOthers()
            );
            tutor.addEducation(education);
        });
    }

    // TODO - CHECK
    // 튜터 탈퇴 시
    public void deleteTutor(User user) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        // TODO - CHECK
        // 수강 중인 강의 없는지 확인
        if (enrollmentRepository.findAllWithLectureTutorByTutorId(tutor.getId()).size() > 0) {
            // TODO - Error Message
            throw new RuntimeException("수강 중인 강의가 존재합니다.");
        }

        lectureRepository.findByTutor(tutor).forEach(lecture -> {
            lectureService.deleteLecture(lecture);
        });

        tutor.quit();
        tutorRepository.delete(tutor);
    }

    private List<Career> getCareers(Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException(TUTOR));
        return careerRepository.findByTutor(tutor);
    }

    @Transactional(readOnly = true)
    public List<CareerResponse> getCareerResponses(Long tutorId) {
        return getCareers(tutorId).stream()
                .map(CareerResponse::new).collect(Collectors.toList());
    }

    private List<Education> getEducations(Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException(TUTOR));
        return educationRepository.findByTutor(tutor);
    }

    @Transactional(readOnly = true)
    public List<EducationResponse> getEducationResponses(Long tutorId) {
        return getEducations(tutorId).stream()
                .map(EducationResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<Lecture> getLectures(User user, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        return lectureRepository.findByTutor(tutor, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public Page<LectureResponse> getLectureResponses(User user, Integer page) {
        // return getLectures(user, page).map(lectureMapstructUtil::getLectureResponse);
        return getLectures(user, page).map(LectureResponse::new);
    }

    private Page<Enrollment> getEnrollmentsOfLecture(User user, Long lectureId, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        return enrollmentRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> getEnrollmentResponsesOfLecture(User user, Long lectureId, Integer page) {
        return getEnrollmentsOfLecture(user, lectureId, page).map(EnrollmentResponse::new);
    }

    private Page<Tutee> getTuteesOfLecture(User user, Long lectureId, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
        // TODO - fetch join
        return enrollmentRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(Enrollment::getTutee);
    }

    @Transactional(readOnly = true)
    public Page<TuteeResponse> getTuteeResponsesOfLecture(User user, Long lectureId, Integer page) {
        return getTuteesOfLecture(user, lectureId, page).map(TuteeResponse::new);
    }


//    @Transactional(readOnly = true)
//    public Page<TuteeResponse> getTuteeResponsesOfLecture(User user, Long lectureId, Integer page) {
//
//        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
//                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));
//
//        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
//                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
//        // TODO - fetch join
//        return enrollmentRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
//                .map(enrollment -> new TuteeResponse(enrollment.getTutee()));
//    }

    private Page<Review> getReviewsOfLecture(User user, Long lectureId, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        return reviewRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

}
