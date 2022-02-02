package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.response.TutorResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class TutorListTest {

    @Autowired
    TutorService tutorService;

    @Disabled
    @Test
    void getTutorResponses() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> START");
        Page<TutorResponse> tutorResponses = tutorService.getTutorResponses(1);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> END");
    }
}
