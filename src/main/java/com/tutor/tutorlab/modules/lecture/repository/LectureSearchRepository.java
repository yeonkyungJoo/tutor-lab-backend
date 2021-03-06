package com.tutor.tutorlab.modules.lecture.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.vo.QTutor;
import com.tutor.tutorlab.modules.account.vo.QUser;
import com.tutor.tutorlab.modules.address.embeddable.Address;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
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
public class LectureSearchRepository {
    // TODO - 테스트
    private final JPAQueryFactory jpaQueryFactory;
    private final QLecture lecture = QLecture.lecture;

    private final QTutor tutor = QTutor.tutor;
    private final QUser user = QUser.user;

    // TODO - CHECK : Lecture에 주소 정보를 넣는 게 더 좋은가? - 튜터 주소 변경 가능성 존재
    private List<Lecture> findLecturesByZone(Address zone) {

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
                    .where(eqState(zone.getState()),
                            eqSiGunGu(zone.getSiGunGu()))
                    .orderBy(lecture.id.asc())
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

    // 강의명으로 검색
    private List<Lecture> findLecturesBySearch(LectureListRequest request) {
        return jpaQueryFactory.selectFrom(lecture)
                .where(eqTitle(request.getTitle()))
                .fetch();
    }

    public Page<Lecture> findLecturesBySearch(LectureListRequest request, Pageable pageable) {

        QueryResults<Lecture> lectures;
        if(request == null) {
            lectures = jpaQueryFactory.selectFrom(lecture)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else {
            lectures = jpaQueryFactory.selectFrom(lecture)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(eqTitle(request.getTitle()))
                    .orderBy(lecture.id.asc())
                    .fetchResults();
        }

        return new PageImpl<>(lectures.getResults(), pageable, lectures.getTotal());
    }

    public Page<Lecture> findLecturesByZoneAndSearch(Address zone, LectureListRequest request, Pageable pageable) {

        QueryResults<Lecture> lectures;
        if (zone == null && request == null) {

            lectures = jpaQueryFactory.selectFrom(lecture)
                    .innerJoin(lecture.tutor, tutor)
                    .fetchJoin()
                    .innerJoin(tutor.user, user)
                    .fetchJoin()
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(lecture.id.asc())
                    .fetchResults();

        } else if(zone == null) {

            lectures = jpaQueryFactory.selectFrom(lecture)
                    .innerJoin(lecture.tutor, tutor)
                    .fetchJoin()
                    .innerJoin(tutor.user, user)
                    .fetchJoin()
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(eqTitle(request.getTitle()),
                            eqSubjects(request.getSubjects()),
                            eqSystemType(request.getSystemType()),
                            eqIsGroup(request.getIsGroup()),
                            eqDifficultyType(request.getDifficultyTypes()))
                    .orderBy(lecture.id.asc())
                    .fetchResults();

        } else if (request == null) {

            lectures = jpaQueryFactory.selectFrom(lecture)
                    .innerJoin(lecture.tutor, tutor)
                    .fetchJoin()
                    .innerJoin(tutor.user, user)
                    .fetchJoin()
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(eqState(zone.getState()),
                            eqSiGunGu(zone.getSiGunGu()))
                    .orderBy(lecture.id.asc())
                    .fetchResults();

        } else {

            lectures = jpaQueryFactory.selectFrom(lecture)
                    .innerJoin(lecture.tutor, tutor)
                    .fetchJoin()
                    .innerJoin(tutor.user, user)
                    .fetchJoin()
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(eqState(zone.getState()),
                            eqSiGunGu(zone.getSiGunGu()),
                            eqTitle(request.getTitle()),
                            eqSubjects(request.getSubjects()),
                            eqSystemType(request.getSystemType()),
                            eqIsGroup(request.getIsGroup()),
                            eqDifficultyType(request.getDifficultyTypes()))
                    .orderBy(lecture.id.asc())
                    .fetchResults();
        }

        return new PageImpl<>(lectures.getResults(), pageable, lectures.getTotal());
    }

    // TODO - 제네릭 사용해서 util로 변경
    private BooleanExpression eqTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return null;
        }
        return lecture.title.eq(title);
    }

    private BooleanExpression eqSubjects(List<String> subjects) {
        if (CollectionUtils.isEmpty(subjects)) {
            return null;
        }
        return lecture.lectureSubjects.any().krSubject.in(subjects);
    }

    private BooleanExpression eqSystemType(SystemType systemType) {
        if (Objects.isNull(systemType)) {
            return null;
        }
        return lecture.systemTypes.contains(systemType);
    }

    private BooleanExpression eqIsGroup(Boolean isGroup) {
        if (Objects.isNull(isGroup)) {
            return null;
        }
        return lecture.lecturePrices.any().isGroup.eq(isGroup);
    }

    private BooleanExpression eqDifficultyType(List<DifficultyType> difficultyTypes) {
        if (CollectionUtils.isEmpty(difficultyTypes)) {
            return null;
        }
        return lecture.difficultyType.in(difficultyTypes);
    }

}
