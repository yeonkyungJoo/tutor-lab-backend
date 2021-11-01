package com.tutor.tutorlab.modules.account.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.vo.QTutee;
import com.tutor.tutorlab.modules.account.vo.QTutor;
import com.tutor.tutorlab.modules.account.vo.QUser;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.lecture.vo.QLecture;
import com.tutor.tutorlab.modules.purchase.vo.QEnrollment;
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
public class TutorQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QTutor tutor = QTutor.tutor;
    private final QTutee tutee = QTutee.tutee;
    private final QUser user = QUser.user;
    private final QEnrollment enrollment = QEnrollment.enrollment;
    private final QLecture lecture = QLecture.lecture;

    public Page<TuteeSimpleResponse> findTuteesOfTutor(Tutor tutor, Pageable pageable) {

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

        List<Long> lectureIds = jpaQueryFactory.select(lecture.id).from(lecture).where(lecture.tutor.eq(tutor)).fetch();
        List<Long> tuteeIds = jpaQueryFactory.select(tutee.id).from(enrollment).where(enrollment.lecture.id.in(lectureIds)).fetch();
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
}
