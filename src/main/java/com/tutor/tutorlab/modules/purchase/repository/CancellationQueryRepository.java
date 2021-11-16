package com.tutor.tutorlab.modules.purchase.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.vo.QTutee;
import com.tutor.tutorlab.modules.account.vo.QUser;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.chat.vo.QChatroom;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.QLecture;
import com.tutor.tutorlab.modules.lecture.vo.QLecturePrice;
import com.tutor.tutorlab.modules.purchase.controller.response.CancellationResponse;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.QCancellation;
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
public class CancellationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QCancellation cancellation = QCancellation.cancellation;
    private final QEnrollment enrollment = QEnrollment.enrollment;
    private final QChatroom chatroom = QChatroom.chatroom;
    private final QLecture lecture = QLecture.lecture;
    private final QLecturePrice lecturePrice = QLecturePrice.lecturePrice;
    private final QTutee tutee = QTutee.tutee;
    private final QUser user = QUser.user;

    public Page<CancellationResponse> findCancellationsOfTutor(Tutor tutor, Pageable pageable) {

        /*
        SELECT * FROM cancellation c
        INNER JOIN enrollment e ON c.enrollment_id = e.enrollment_id
        INNER JOIN lecture l ON e.lecture_id = l.lecture_id
        INNER JOIN lecture_price lp ON e.lecture_price_id = lp.lecture_price_id
        INNER JOIN tutee t ON e.tutee_id = t.tutee_id
        INNER JOIN user u ON t.user_id = u.user_id
        */
        QueryResults<Tuple> tuples = jpaQueryFactory.select(cancellation, lecture, lecturePrice, tutee.id, user.name)
                .from(cancellation)
                .innerJoin(cancellation.enrollment, enrollment)
                .innerJoin(enrollment.lecture, lecture)
                .innerJoin(enrollment.lecturePrice, lecturePrice)
                .innerJoin(enrollment.tutee, tutee)
                .innerJoin(tutee.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(lecture.tutor.eq(tutor))
                .fetchResults();

/*        jpaQueryFactory.select(chatroom.id)
                .from(chatroom)
                .innerJoin(chatroom.enrollment, enrollment)*/

        List<CancellationResponse> cancellationResponses = tuples.getResults().stream()
                .map(tuple -> CancellationResponse.builder()
                        .cancellation(tuple.get(0, Cancellation.class))
                        .lecture(tuple.get(1, Lecture.class))
                        .lecturePrice(tuple.get(2, LecturePrice.class))
                        .tuteeId(tuple.get(3, Long.class))
                        .tuteeName(tuple.get(4, String.class))
                        .build()).collect(Collectors.toList());
        return new PageImpl<>(cancellationResponses, pageable, tuples.getTotal());
    }

}
