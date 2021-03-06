package com.tutor.tutorlab.modules.inquiry.repository;

import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.inquiry.enums.InquiryType;
import com.tutor.tutorlab.modules.inquiry.vo.Inquiry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class InquiryRepositoryTest {

    @Autowired
    InquiryRepository inquiryRepository;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void init() {

        user = userRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);

    }

    // TODO - CHECK
    // Converter
    @Test
    void findAll() {

        // given
        Inquiry inquiry = Inquiry.of(user, InquiryType.LECTURE, "title", "content");
        inquiryRepository.save(inquiry);

        // when
        List<Inquiry> inquiries = inquiryRepository.findAll();
        // then
        assertAll(
                () -> assertThat(inquiries.size()).isEqualTo(1),
                () -> assertThat(inquiries.get(0)).extracting("type").isEqualTo(inquiry.getType()),
                () -> assertThat(inquiries.get(0)).extracting("title").isEqualTo(inquiry.getTitle()),
                () -> assertThat(inquiries.get(0)).extracting("content").isEqualTo(inquiry.getContent())
        );
    }
}