package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CancellationRepository extends JpaRepository<Cancellation, Long> {

    Cancellation findByEnrollment(Enrollment enrollment);
}
