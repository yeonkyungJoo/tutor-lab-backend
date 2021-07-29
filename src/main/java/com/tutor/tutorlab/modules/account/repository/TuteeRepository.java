package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TuteeRepository extends JpaRepository<Tutee, Long> {

    Tutee findByUser(User user);
}
