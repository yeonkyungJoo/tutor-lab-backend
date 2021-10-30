package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.lecture.repository.dto.LectureReviewQueryDto;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureTutorQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class LectureQueryRepository {

    private final EntityManager em;

    /*
    SELECT r.lecture_id, COUNT(r.review_id), ROUND(AVG(r.score), 2) FROM review r
    WHERE r.enrollment_id IS NOT NULL
    -- AND r.lecture_id = 22
    GROUP BY r.lecture_id
     */
    public Map<Long, LectureReviewQueryDto> findLectureReviewQueryDtoMap(List<Long> lectureIds) {
        List<LectureReviewQueryDto> lectureReviews = em.createQuery("select new com.tutor.tutorlab.modules.lecture.repository.dto.LectureReviewQueryDto(r.lecture.id, count(r.id), avg(r.score)) from Review r " +
                "where r.enrollment is not null and r.lecture.id in :lectureIds group by r.lecture", LectureReviewQueryDto.class)
                .setParameter("lectureIds", lectureIds).getResultList();

        return lectureReviews.stream()
                .collect(Collectors.toMap(LectureReviewQueryDto::getLectureId, lectureReviewQueryDto -> lectureReviewQueryDto));
//        return lectureReviews.stream()
//                .collect(Collectors.groupingBy(LectureReviewQueryDto::getLectureId));
    }

    /*
    SELECT t.tutor_id, COUNT(DISTINCT l.lecture_id), COUNT(DISTINCT r.review_id) FROM lecture l
    INNER JOIN tutor t ON l.tutor_id = t.tutor_id
    LEFT JOIN review r ON l.lecture_id = r.lecture_id AND r.enrollment_id IS NOT NULL
    GROUP BY t.tutor_id
     */
    public Map<Long, LectureTutorQueryDto> findLectureTutorQueryDtoMap(List<Long> lectureIds) {
        List<LectureTutorQueryDto> lectureTutors = em.createQuery("select new com.tutor.tutorlab.modules.lecture.repository.dto.LectureTutorQueryDto(t.id, count(distinct l.id), count(distinct r.id)) from Lecture l " +
                "inner join Tutor t on l.tutor.id = t.id " +
                "left join Review r on l.id = r.lecture.id and r.enrollment is not null " +
                "where l.id in :lectureIds group by t.id", LectureTutorQueryDto.class).setParameter("lectureIds", lectureIds).getResultList();

        return lectureTutors.stream().collect(Collectors.toMap(LectureTutorQueryDto::getTutorId, lectureTutorQueryDto -> lectureTutorQueryDto));
    }
}
