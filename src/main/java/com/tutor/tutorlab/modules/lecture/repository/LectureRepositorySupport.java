package com.tutor.tutorlab.modules.lecture.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.QLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class LectureRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    private final QLecture lecture = QLecture.lecture;

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
