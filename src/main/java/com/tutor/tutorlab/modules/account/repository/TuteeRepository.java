package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TuteeRepository extends JpaRepository<Tutee, Long> {
}
