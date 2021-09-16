package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TuteeServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    TuteeRepository tuteeRepository;
    @Autowired
    TuteeService tuteeService;
    @Autowired
    UserRepository userRepository;

    @Transactional
    @DisplayName("Tutee 삭제 - CascadeType.MERGE")
    @WithAccount("yk")
    @Test
    void deleteTutee() {

        // Given
        User user = userRepository.findByName("yk");

        // When
        tuteeService.deleteTutee(user);
//        em.flush();
//
//        // Then
//        assertTrue(user.isDeleted());
//        assertNotNull(user.getDeletedAt());
//        assertNull(tuteeRepository.findByUser(user));
//        assertEquals(0, tuteeRepository.count());

    }
}