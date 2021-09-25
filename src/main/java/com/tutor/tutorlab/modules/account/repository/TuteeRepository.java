package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TuteeRepository extends JpaRepository<Tutee, Long> {

    Tutee findByUser(User user);
}
