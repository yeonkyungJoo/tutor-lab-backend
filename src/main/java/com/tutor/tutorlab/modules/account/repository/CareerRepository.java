package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CareerRepository extends JpaRepository<Career, Long> {

    Optional<Career> findByTutorAndId(Tutor tutor, Long careerId);
    List<Career> findByTutor(Tutor tutor);

    @Transactional
    void deleteByTutor(Tutor tutor);
}
