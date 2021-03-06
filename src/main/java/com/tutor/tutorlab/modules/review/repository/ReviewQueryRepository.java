package com.tutor.tutorlab.modules.review.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.vo.QUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureReviewQueryDto;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureTutorQueryDto;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.QLecture;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.controller.response.ReviewWithSimpleLectureResponse;
import com.tutor.tutorlab.modules.review.vo.QReview;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.Data;
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
import java.util.stream.Stream;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class ReviewQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QReview review = QReview.review;
    private final QUser user = QUser.user;
    private final QLecture lecture = QLecture.lecture;

    private final EntityManager em;

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

    // TODO - ????????? ??????
    private BooleanExpression eqLecture(Lecture lecture) {
        if (lecture == null) {
            return null;
        }
        return review.lecture.eq(lecture);
    }

    private BooleanExpression eqUser(User user) {
        if (user == null) {
            return null;
        }
        return review.user.eq(user);
    }

    // SELECT * FROM review r LEFT OUTER JOIN review p ON r.review_id = p.parent_id
    // AND r.parent_id IS NULL
    public Page<ReviewResponse> findReviewsWithChildByLecture(Lecture lecture, Pageable pageable) {

        QueryResults<Review> parents = jpaQueryFactory.selectFrom(review)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(eqLecture(lecture), review.parent.isNull())
                .fetchResults();

        List<Long> parentIds = parents.getResults().stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<Review> children = em.createQuery("select r from Review r where r.parent is not null and r.parent.id in :parentIds", Review.class)
                .setParameter("parentIds", parentIds).getResultList();

        Map<Long, Review> map = children.stream()
                .collect(Collectors.toMap(child -> child.getParent().getId(), child -> child));
        List<ReviewResponse> results = parents.getResults().stream()
                .map(parent -> new ReviewResponse(parent, map.get(parent.getId()))).collect(Collectors.toList());

        return new PageImpl<>(results, pageable, parents.getTotal());
    }

    // TODO - with User
//    public Page<ReviewResponse> findReviewsWithChildByUser(User user, Pageable pageable) {
//
//        QueryResults<Review> parents = jpaQueryFactory.selectFrom(review)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .where(eqUser(user), review.parent.isNull())
//                .fetchResults();
//
//        List<Long> parentIds = parents.getResults().stream().map(BaseEntity::getId).collect(Collectors.toList());
//        List<Review> children = em.createQuery("select r from Review r where r.parent is not null and r.parent.id in :parentIds", Review.class)
//                .setParameter("parentIds", parentIds).getResultList();
//
//        Map<Long, Review> map = children.stream()
//                .collect(Collectors.toMap(child -> child.getParent().getId(), child -> child));
//        List<ReviewResponse> results = parents.getResults().stream()
//                .map(parent -> new ReviewResponse(parent, map.get(parent.getId()))).collect(Collectors.toList());
//
//        return new PageImpl<>(results, pageable, parents.getTotal());
//    }

    public Page<ReviewWithSimpleLectureResponse> findReviewsWithChildAndSimpleLectureByUser(User user, Pageable pageable) {

        QueryResults<Review> parents = jpaQueryFactory.selectFrom(review)
                .innerJoin(review.lecture, lecture)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(eqUser(user), review.parent.isNull())
                .fetchResults();

        List<Long> parentIds = parents.getResults().stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<Review> children = em.createQuery("select r from Review r where r.parent is not null and r.parent.id in :parentIds", Review.class)
                .setParameter("parentIds", parentIds).getResultList();

        Map<Long, Review> map = children.stream()
                .collect(Collectors.toMap(child -> child.getParent().getId(), child -> child));
        List<ReviewWithSimpleLectureResponse> results = parents.getResults().stream()
                .map(parent -> new ReviewWithSimpleLectureResponse(parent, map.get(parent.getId()))).collect(Collectors.toList());

        return new PageImpl<>(results, pageable, parents.getTotal());
    }

}
