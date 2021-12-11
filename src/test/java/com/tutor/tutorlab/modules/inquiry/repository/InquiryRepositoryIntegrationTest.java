package com.tutor.tutorlab.modules.inquiry.repository;

import com.tutor.tutorlab.modules.inquiry.vo.Inquiry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class InquiryRepositoryIntegrationTest {

    @Autowired
    InquiryRepository inquiryRepository;

    // TODO - CHECK
    // Converter
    @Test
    void findAll() {
        List<Inquiry> inquiries = inquiryRepository.findAll();
        inquiries.stream().forEach(inquiry -> System.out.println(inquiry.getType()));
    }
}