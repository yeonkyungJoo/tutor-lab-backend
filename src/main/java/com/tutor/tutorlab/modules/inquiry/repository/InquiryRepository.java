package com.tutor.tutorlab.modules.inquiry.repository;

import com.tutor.tutorlab.modules.inquiry.vo.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
