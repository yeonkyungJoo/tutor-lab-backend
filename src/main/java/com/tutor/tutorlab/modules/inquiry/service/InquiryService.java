package com.tutor.tutorlab.modules.inquiry.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.inquiry.controller.request.InquiryCreateRequest;
import com.tutor.tutorlab.modules.inquiry.repository.InquiryRepository;
import com.tutor.tutorlab.modules.inquiry.vo.Inquiry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Inquiry createInquiry(User user, InquiryCreateRequest inquiryCreateRequest) {

        Inquiry inquiry = Inquiry.of(user,
                inquiryCreateRequest.getInquiryType(),
                inquiryCreateRequest.getTitle(),
                inquiryCreateRequest.getContent());
        return inquiryRepository.save(inquiry);
    }
}
