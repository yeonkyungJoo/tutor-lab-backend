package com.tutor.tutorlab.modules.review.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.vo.QUser;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureReviewQueryDto;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureTutorQueryDto;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.review.vo.QReview;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class ReviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QReview review = QReview.review;

    private final QUser user  = QUser.user;

    public Page<Review> findReviewsWithUserByLecture(Lecture lecture, Pageable pageable) {

        QueryResults<Review> reviews = jpaQueryFactory.selectFrom(review)
                .innerJoin(review.user, user)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(eqLecture(lecture))
                .fetchResults();
        return new PageImpl<>(reviews.getResults(), pageable, reviews.getTotal());
    }

    private BooleanExpression eqLecture(Lecture lecture) {
        if (lecture == null) {
            return null;
        }
        return review.lecture.eq(lecture);
    }


}
