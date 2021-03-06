package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.response.PickResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Disabled
@Transactional
@SpringBootTest
public class PickListTest {

    @Autowired
    PickService pickService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    private User user;

    @BeforeEach
    void init() {
        user = userRepository.findByUsername("user1@email.com").orElse(null);
        pickService.createPick(user, 22L);
        pickService.createPick(user, 23L);
        pickService.createPick(user, 24L);
        em.clear();
    }

    @Test
    void getPickResponses() {
        user = userRepository.findByUsername("user1@email.com").orElse(null);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> START");
        Page<PickResponse> pickResponses = pickService.getPickResponses(user, 1);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> END");
    }
}
