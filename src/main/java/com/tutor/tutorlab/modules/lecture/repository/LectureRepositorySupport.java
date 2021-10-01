package com.tutor.tutorlab.modules.lecture.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.vo.QTutor;
import com.tutor.tutorlab.modules.account.vo.QUser;
import com.tutor.tutorlab.modules.address.embeddable.Address;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.QLecture;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class LectureRepositorySupport {
    // TODO - 테스트
    private final JPAQueryFactory jpaQueryFactory;
    private final QLecture lecture = QLecture.lecture;

    private final QTutor tutor = QTutor.tutor;
    private final QUser user = QUser.user;

    // TODO - CHECK : Lecture에 주소 정보를 넣는 게 더 좋은가?
    public List<Lecture> findLecturesByZone(Address zone) {

        if (zone == null) {
            return jpaQueryFactory.selectFrom(lecture).fetch();
        }
        return jpaQueryFactory.selectFrom(lecture)
                .innerJoin(lecture.tutor, tutor)
                .innerJoin(tutor.user, user)
                .where(eqState(zone.getState()),
                        eqSiGunGu(zone.getSiGunGu()))
                .fetch();
    }

    public Page<Lecture> findLecturesByZone(Address zone, Pageable pageable) {

        QueryResults<Lecture> lectures = QueryResults.emptyResults();
        if (zone == null) {
            lectures = jpaQueryFactory.selectFrom(lecture)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else {
            lectures = jpaQueryFactory.selectFrom(lecture)
                    .innerJoin(lecture.tutor, tutor)
                    .innerJoin(tutor.user, user)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(lecture.id.asc())
                    .where(eqState(zone.getState()),
                            eqSiGunGu(zone.getSiGunGu()))
                    .fetchResults();
        }

        // PageImpl(List<T> content, Pageable pageable, long total)
        return new PageImpl<>(lectures.getResults(), pageable, lectures.getTotal());

    }

    private BooleanExpression eqState(String state) {
        if (StringUtils.isBlank(state)) {
            return null;
        }
        return user.zone.state.eq(state);
    }

    private BooleanExpression eqSiGunGu(String siGunGu) {
        if (StringUtils.isBlank(siGunGu)) {
            return null;
        }
        return user.zone.siGunGu.eq(siGunGu);
    }

    public List<Lecture> findLecturesBySearch(LectureListRequest request) {
        return jpaQueryFactory.selectFrom(lecture)
                .where(eqDifficulty(request.getDifficulties()),
                       eqSystemTypes(request.getSystems()),
                       eqIsGroup(request.getIsGroup()),
                       eqParents(request.getParents()),
                       eqSubjects(request.getSubjects()))
                .fetch();
    }

    private BooleanExpression eqDifficulty(List<DifficultyType> difficulty) {
        if (CollectionUtils.isEmpty(difficulty)) {
            return null;
        }
        return lecture.difficultyType.in(difficulty);
    }

    private BooleanExpression eqSystemTypes(List<SystemType> systemTypes) {
        if (CollectionUtils.isEmpty(systemTypes)) {
            return null;
        }
        return lecture.systemTypes.any().in(systemTypes);
    }

    private BooleanExpression eqIsGroup(Boolean isGroup) {
        if (Objects.isNull(isGroup)) {
            return null;
        }
        return lecture.lecturePrices.any().isGroup.eq(isGroup);
    }

    private BooleanExpression eqParents(List<String> parents) {
        if (CollectionUtils.isEmpty(parents)) {
            return null;
        }
        return lecture.lectureSubjects.any().parent.in(parents);
    }

    private BooleanExpression eqSubjects(List<String> subjects) {
        if (CollectionUtils.isEmpty(subjects)) {
            return null;
        }
        return lecture.lectureSubjects.any().krSubject.in(subjects);
    }
}
