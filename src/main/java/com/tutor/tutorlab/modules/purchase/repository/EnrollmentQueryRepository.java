package com.tutor.tutorlab.modules.purchase.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.lecture.vo.QLecture;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentWithSimpleLectureResponse;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.purchase.vo.QEnrollment;
import com.tutor.tutorlab.modules.review.vo.QReview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class EnrollmentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QEnrollment enrollment = QEnrollment.enrollment;
    private final QReview review = QReview.review;

    private final QLecture lecture = QLecture.lecture;

    // SELECT * FROM enrollment e
    // INNER JOIN lecture l ON e.lecture_id = l.lecture_id
    // WHERE EXISTS (SELECT enrollment_id, lecture_id FROM review r WHERE e.enrollment_id = r.enrollment_id AND e.lecture_id = r.lecture_id)
    public Page<EnrollmentWithSimpleLectureResponse> findEnrollments(Tutee tutee, boolean reviewed, Pageable pageable) {

        QueryResults<Enrollment> enrollments = jpaQueryFactory.selectFrom(enrollment)
                .innerJoin(enrollment.lecture, lecture)
                .fetchJoin()
                .where(reviewed ? JPAExpressions.selectFrom(review).where(review.enrollment.eq(enrollment)).exists() : JPAExpressions.selectFrom(review).where(review.enrollment.eq(enrollment)).notExists(),
                        enrollment.tutee.eq(tutee))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<EnrollmentWithSimpleLectureResponse> results = enrollments.getResults().stream().map(EnrollmentWithSimpleLectureResponse::new)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, enrollments.getTotal());
    }

}
