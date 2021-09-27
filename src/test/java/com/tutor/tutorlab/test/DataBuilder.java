package com.tutor.tutorlab.test;

import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;

import java.util.Arrays;

public class DataBuilder {

    public static SignUpRequest getSignUpRequest(String name) {
        return SignUpRequest.builder()
                .username(name + "@email.com")
                .password("password")
                .passwordConfirm("password")
                .name(name)
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
    }

    public static CareerCreateRequest getCareerCreateRequest(String companyName, String duty) {
        return CareerCreateRequest.builder()
                .companyName(companyName)
                .duty(duty)
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
    }

    public static EducationCreateRequest getEducationCreateRequest(String schoolName, String major) {
        return EducationCreateRequest.builder()
                .schoolName(schoolName)
                .major(major)
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
    }

    public static TutorSignUpRequest getTutorSignUpRequest(String subjects, String companyName, String duty, String schoolName, String major) {
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects(subjects)
                .specialist(false)
                .build();
        tutorSignUpRequest.addCareerCreateRequest(getCareerCreateRequest(companyName, duty));
        tutorSignUpRequest.addEducationCreateRequest(getEducationCreateRequest(schoolName, major));
        return tutorSignUpRequest;
    }

    public static LectureCreateRequest.LecturePriceCreateRequest getLecturePriceCreateRequest(Long pertimeCost, Integer pertimeLecture, Integer totalTime) {
        return LectureCreateRequest.LecturePriceCreateRequest.builder()
                .isGroup(true)
                .groupNumber(3)
                .pertimeCost(pertimeCost)
                .pertimeLecture(pertimeLecture)
                .totalCost(pertimeCost * pertimeLecture)
                .totalTime(totalTime)
                .build();
    }

    public static LectureCreateRequest.LectureSubjectCreateRequest getLectureSubjectCreateRequest(String krSubject) {
        return LectureCreateRequest.LectureSubjectCreateRequest.builder()
                .parent("개발")
                .krSubject(krSubject)
                .build();
    }

    public static LectureCreateRequest getLectureCreateRequest(String title, Long pertimeCost, Integer pertimeLecture, Integer totalTime, String krSubject) {

        LectureCreateRequest.LecturePriceCreateRequest price1 = getLecturePriceCreateRequest(pertimeCost, pertimeLecture, totalTime);
        LectureCreateRequest.LectureSubjectCreateRequest subject1 = getLectureSubjectCreateRequest(krSubject);

        return LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title(title)
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE))
                .lecturePrices(Arrays.asList(price1))
                .subjects(Arrays.asList(subject1))
                .build();
    }

    public static TuteeReviewCreateRequest getTuteeReviewCreateRequest(Integer score, String content) {
        return TuteeReviewCreateRequest.builder()
                .score(score)
                .content(content)
                .build();
    }

    public static TuteeReviewUpdateRequest getTuteeReviewUpdateRequest(Integer score, String content) {
        return TuteeReviewUpdateRequest.builder()
                .score(score)
                .content(content)
                .build();
    }

    public static TutorReviewCreateRequest getTutorReviewCreateRequest(String content) {
        return TutorReviewCreateRequest.builder()
                .content(content)
                .build();
    }

    public static TutorReviewUpdateRequest getTutorReviewUpdateRequest(String content) {
        return TutorReviewUpdateRequest.builder()
                .content(content)
                .build();
    }
}
