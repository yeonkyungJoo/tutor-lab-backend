package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface EducationRepository extends JpaRepository<Education, Long> {

    Optional<Education> findByTutorAndId(Tutor tutor, Long educationId);
    List<Education> findByTutor(Tutor tutor);
}
