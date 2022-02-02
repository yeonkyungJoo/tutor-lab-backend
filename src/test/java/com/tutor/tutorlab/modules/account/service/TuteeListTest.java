package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class TuteeListTest {

    @Autowired
    TuteeService tuteeService;

    @Disabled
    @Test
    void getTuteeResponses() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> START");
        Page<TuteeResponse> tuteeResponses = tuteeService.getTuteeResponses(1);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> END");
    }
}
