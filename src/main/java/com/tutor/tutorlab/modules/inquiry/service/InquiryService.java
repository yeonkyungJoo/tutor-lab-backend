package com.tutor.tutorlab.modules.inquiry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.messageQueue.Producer;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
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
    private final UserRepository userRepository;

    private final Producer producer;
    private final ObjectMapper objectMapper;

    static final String ROUTING_KEY = "CREATE_INQUIRY_QUEUE";

    public Inquiry createInquiry(User user, InquiryCreateRequest inquiryCreateRequest) {

        Inquiry inquiry = Inquiry.of(user,
                inquiryCreateRequest.getInquiryType(),
                inquiryCreateRequest.getTitle(),
                inquiryCreateRequest.getContent());
        return inquiryRepository.save(inquiry);
    }

    public Inquiry test(InquiryCreateRequest inquiryCreateRequest) throws JsonProcessingException {

        User user = userRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);

        Inquiry inquiry = Inquiry.of(user,
                inquiryCreateRequest.getInquiryType(),
                inquiryCreateRequest.getTitle(),
                inquiryCreateRequest.getContent());

        String message = objectMapper.writeValueAsString(inquiry);
        producer.send(ROUTING_KEY, message);

        return inquiry;
    }
}
