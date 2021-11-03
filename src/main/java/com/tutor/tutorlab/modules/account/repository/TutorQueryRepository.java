package com.tutor.tutorlab.modules.account.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.controller.response.TuteeLectureResponse;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.vo.QTutee;
import com.tutor.tutorlab.modules.account.vo.QTutor;
import com.tutor.tutorlab.modules.account.vo.QUser;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.QLecture;
import com.tutor.tutorlab.modules.lecture.vo.QLecturePrice;
import com.tutor.tutorlab.modules.purchase.vo.QEnrollment;
import com.tutor.tutorlab.modules.review.vo.QReview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class TutorQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QTutor tutor = QTutor.tutor;
    private final QTutee tutee = QTutee.tutee;
    private final QUser user = QUser.user;
    private final QEnrollment enrollment = QEnrollment.enrollment;
    private final QLecture lecture = QLecture.lecture;
    private final QLecturePrice lecturePrice = QLecturePrice.lecturePrice;

    private final QReview review = QReview.review;

    /*
    SELECT u.user_id, u.name, te.tutee_id FROM tutee te
    INNER JOIN user u ON te.user_id = u.user_id
    WHERE te.tutee_id IN
        (SELECT tutee_id FROM enrollment e WHERE e.lecture_id IN
            (SELECT lecture_id FROM lecture l WHERE l.tutor_id = 1));

    SELECT u.user_id, u.name, te.tutee_id, COUNT(l.lecture_id) FROM tutee te
    INNER JOIN user u ON te.user_id = u.user_id
    INNER JOIN enrollment e ON te.tutee_id = e.tutee_id
    INNER JOIN lecture l ON e.lecture_id = l.lecture_id
    WHERE l.tutor_id = 1
    GROUP BY u.user_id, u.name, te.tutee_id;

    SELECT e.tutee_id, count(e.lecture_id) FROM enrollment e
    INNER JOIN lecture l ON e.lecture_id = l.lecture_id
    WHERE l.tutor_id = 1
    GROUP BY e.tutee_id;
    */

    public Page<TuteeSimpleResponse> findTuteesOfTutor(Tutor tutor, Boolean closed, Pageable pageable) {

        // TODO - CHECK
        // TODO - 서브쿼리 효율성
        /*
        SELECT u.user_id, u.name, te.tutee_id FROM tutee te
        INNER JOIN user u ON te.user_id = u.user_id
        WHERE te.tutee_id IN
        (SELECT tutee_id FROM enrollment e WHERE e.lecture_id IN
        (SELECT lecture_id FROM lecture l WHERE l.tutor_id = 1));

        QueryResults<Tuple> tutees = jpaQueryFactory.select(tutee.id, tutee.user.id, tutee.user.name)
                .from(tutee)
                .innerJoin(tutee.user, user)
                //.fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(tutee.id.in(
                        JPAExpressions.select(tutee.id).from(enrollment).where(enrollment.lecture.id.in(
                                JPAExpressions.select(lecture.id).from(lecture).where(lecture.tutor.eq(tutor))))))
                .fetchResults();
        */

        List<Long> lectureIds = jpaQueryFactory.select(lecture.id)
                .from(lecture)
                .where(lecture.tutor.eq(tutor))
                .fetch();
        List<Long> tuteeIds = jpaQueryFactory.select(tutee.id)
                .from(enrollment)
                .where(isClosed(closed),
                        enrollment.canceled.isFalse(),
                        enrollment.lecture.id.in(lectureIds))
                .fetch();

        QueryResults<Tuple> tuples = jpaQueryFactory.select(tutee.id, tutee.user.id, tutee.user.name)
                .from(tutee)
                .innerJoin(tutee.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(tutee.id.in(tuteeIds))
                .fetchResults();

        List<TuteeSimpleResponse> tuteeSimpleResponses = tuples.getResults().stream()
                .map(tuple -> TuteeSimpleResponse.builder()
                        .tuteeId(tuple.get(0, Long.class))
                        .userId(tuple.get(1, Long.class))
                        .name(tuple.get(2, String.class))
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(tuteeSimpleResponses, pageable, tuples.getTotal());
    }

    private BooleanExpression isClosed(Boolean closed) {
        if (closed == null) {
            return null;
        }
        return closed ? enrollment.closed.isTrue() : enrollment.closed.isFalse();
    }

    // TODO - CHECK
    public Page<TuteeLectureResponse> findTuteeLecturesOfTutor(Tutor tutor, Boolean closed, Long tuteeId, PageRequest pageable) {

        /*
            SELECT * FROM enrollment e
            INNER JOIN lecture_price lp ON e.lecture_price_id = lp.lecture_price_id
            INNER JOIN lecture l ON lp.lecture_id = l.lecture_id
            LEFT OUTER JOIN review r ON r.enrollment_id = e.enrollment_id
            WHERE e.tutee_id = 2 AND e.closed = 0 AND e.canceled = 0 AND l.tutor_id = 1;
        */

        QueryResults<Tuple> tuples = jpaQueryFactory.select(
                lecture, lecturePrice, review.id)
                .from(enrollment)
                .innerJoin(enrollment.lecturePrice, lecturePrice)
                .innerJoin(lecturePrice.lecture, lecture)
                .leftJoin(review).on(enrollment.eq(review.enrollment))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(enrollment.tutee.id.eq(tuteeId),
                        isClosed(closed),
                        enrollment.canceled.isFalse(),
                        lecture.tutor.eq(tutor))
                .fetchResults();

        List<TuteeLectureResponse> tuteeLectureResponses = tuples.getResults().stream()
                .map(tuple -> TuteeLectureResponse.builder()
                        .tuteeId(tuteeId)
                        .lecture(tuple.get(0, Lecture.class))
                        .lecturePrice(tuple.get(1, LecturePrice.class))
                        .reviewId(tuple.get(2, Long.class))
                        .build()).collect(Collectors.toList());

        return new PageImpl<>(tuteeLectureResponses, pageable, tuples.getTotal());
    }

}
