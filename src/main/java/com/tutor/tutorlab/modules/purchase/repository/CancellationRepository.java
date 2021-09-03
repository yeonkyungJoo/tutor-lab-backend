package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancellationRepository extends JpaRepository<Cancellation, Long> {
}
