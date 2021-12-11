package com.tutor.tutorlab.modules.inquiry.service;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.inquiry.controller.request.InquiryCreateRequest;
import com.tutor.tutorlab.modules.inquiry.enums.InquiryType;
import com.tutor.tutorlab.modules.inquiry.repository.InquiryRepository;
import com.tutor.tutorlab.modules.inquiry.vo.Inquiry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

// @Transactional
@SpringBootTest
class InquiryServiceIntegrationTest extends AbstractTest {

    @Autowired
    InquiryService inquiryService;
    @Autowired
    InquiryRepository inquiryRepository;

    @Autowired
    UserRepository userRepository;

    @WithAccount(NAME)
    @Test
    void createInquiry() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);

        // When
        InquiryCreateRequest inquiryCreateRequest = InquiryCreateRequest.of(InquiryType.ETC, "제목", "내용");
        Long inquiryId = inquiryService.createInquiry(user, inquiryCreateRequest).getId();

        // Then
        assertEquals(1, inquiryRepository.count());
        assertTrue(inquiryRepository.findById(inquiryId).isPresent());

        Inquiry inquiry = inquiryRepository.findById(inquiryId).get();
        assertAll(
                () -> assertEquals(inquiryCreateRequest.getInquiryType(), inquiry.getType()),
                () -> assertEquals(inquiryCreateRequest.getTitle(), inquiry.getTitle()),
                () -> assertEquals(inquiryCreateRequest.getContent(), inquiry.getContent())
        );
    }
}