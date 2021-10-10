package com.tutor.tutorlab.config.init;

import com.tutor.tutorlab.modules.account.controller.request.*;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.LearningKindType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;

import java.util.Arrays;

public class TestDataBuilder {

    public static User getUser(String name) {
        return User.of(
                name + "@email.com",
                "password",
                name,
                "MALE",
                null,
                null,
                null,
                name,
                null,
                "서울특별시 강남구 삼성동",
                null,
                RoleType.TUTEE,
                null,
                null
        );
    }

    public static SignUpRequest getSignUpRequest(String name, String zone) {
        return SignUpRequest.of(
                name + "@email.com",
                "password",
                "password",
                name,
                "FEMALE",
                null,
                null,
                null,
                name,
                null,
                zone,
                null
        );
    }

    public static SignUpOAuthDetailRequest getSignUpOAuthDetailRequest(String nickname) {
        return SignUpOAuthDetailRequest.of(
                "FEMALE",
                null,
                "010-1234-5678",
                null,
                nickname,
                "hello",
                "서울특별시 강남구 삼성동",
                null
        );
    }

    public static TuteeUpdateRequest getTuteeUpdateRequest(String subjects) {
        return TuteeUpdateRequest.of(subjects);
    }

    public static CareerCreateRequest getCareerCreateRequest(String companyName, String duty) {
        return CareerCreateRequest.of(
                companyName,
                duty,
                "2007-12-03",
                "2007-12-04",
                false
        );
    }

    public static CareerUpdateRequest getCareerUpdateRequest(String companyName, String duty, String startDate, String endDate, boolean present) {
        return CareerUpdateRequest.of(
                companyName,
                duty,
                startDate,
                endDate,
                present
        );
    }

    public static EducationCreateRequest getEducationCreateRequest(String schoolName, String major) {
        return EducationCreateRequest.of(
                schoolName,
                major,
                "2021-01-01",
                "2021-02-01",
                4.01,
                "Bachelor"
        );
    }

    public static EducationUpdateRequest getEducationUpdateRequest(String schoolName, String major,
                                                                   String entranceDate, String graduationDate, double score, String degree) {
        return EducationUpdateRequest.of(
                schoolName,
                major,
                entranceDate,
                graduationDate,
                score,
                degree
        );
    }

    public static TutorSignUpRequest getTutorSignUpRequest(String subjects) {
        return TutorSignUpRequest.of(
                subjects,
                false
        );
    }

    public static TutorSignUpRequest getTutorSignUpRequest(String subjects, String companyName, String duty, String schoolName, String major) {
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.of(
                subjects,
                false
        );
        tutorSignUpRequest.addCareerCreateRequest(getCareerCreateRequest(companyName, duty));
        tutorSignUpRequest.addEducationCreateRequest(getEducationCreateRequest(schoolName, major));
        return tutorSignUpRequest;
    }

    public static TutorUpdateRequest getTutorUpdateRequest(String subjects, boolean specialist) {
        return TutorUpdateRequest.of(subjects, specialist);
    }

    public static LectureCreateRequest.LecturePriceCreateRequest getLecturePriceCreateRequest(Long pertimeCost, Integer pertimeLecture, Integer totalTime) {
        return LectureCreateRequest.LecturePriceCreateRequest.of(
                true,
                3,
                pertimeCost,
                pertimeLecture,
                totalTime,
                pertimeCost * pertimeLecture
        );
    }

    public static LectureCreateRequest.LectureSubjectCreateRequest getLectureSubjectCreateRequest(LearningKindType type, String krSubject) {
        return LectureCreateRequest.LectureSubjectCreateRequest.of(type, krSubject);
    }

    public static LectureCreateRequest getLectureCreateRequest(String title, Long pertimeCost, Integer pertimeLecture, Integer totalTime, LearningKindType type, String krSubject) {

        LectureCreateRequest.LecturePriceCreateRequest price = getLecturePriceCreateRequest(pertimeCost, pertimeLecture, totalTime);
        LectureCreateRequest.LectureSubjectCreateRequest subject = getLectureSubjectCreateRequest(type, krSubject);

        return LectureCreateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                title,
                "소제목",
                "소개",
                DifficultyType.BEGINNER,
                "<p>본문</p>",
                Arrays.asList(SystemType.ONLINE),
                Arrays.asList(price),
                Arrays.asList(subject)
        );
    }

    public static TuteeReviewCreateRequest getTuteeReviewCreateRequest(Integer score, String content) {
        return TuteeReviewCreateRequest.of(score, content);
    }

    public static TuteeReviewUpdateRequest getTuteeReviewUpdateRequest(Integer score, String content) {
        return TuteeReviewUpdateRequest.of(score, content);
    }

    public static TutorReviewCreateRequest getTutorReviewCreateRequest(String content) {
        return TutorReviewCreateRequest.of(content);
    }

    public static TutorReviewUpdateRequest getTutorReviewUpdateRequest(String content) {
        return TutorReviewUpdateRequest.of(content);
    }

    public static LoginRequest getLoginRequest(String username, String password) {
        return LoginRequest.of(username, password);
    }
}
