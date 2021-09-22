package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    // Optional<Tutor> findByUser(User user);
    Tutor findByUser(User user);
}
