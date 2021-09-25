package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    // Optional<Tutor> findByUser(User user);
    Tutor findByUser(User user);
}
