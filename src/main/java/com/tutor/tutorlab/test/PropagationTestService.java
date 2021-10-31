package com.tutor.tutorlab.test;

import com.tutor.tutorlab.config.init.TestDataBuilder;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PropagationTestService {

    private final UserRepository userRepository;
    private final TutorService tutorService;

    // * Propagation(전파 방식)
    // 1. Propagation.REQUIRED (default)
    // - 기존에 사용하던 트랜잭션이 있다면 그 트랜잭션을 사용하고
    // 트랜잭션이 없다면 새로 트랜잭션을 생성해서 사용

    // 2. Propagation.REQUIRES_NEW
    // - 기존에 사용하던 트랜잭션과 상관없이 새로운 트랜잭션을 생성
    // - 호출하는 쪽의 커밋/롤백과 상관없이 자체적으로 커밋/롤백 진행

    // @Transactional(propagation = Propagation.REQUIRED)
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    // @Transactional(propagation = Propagation.NESTED)
    // @Transactional(propagation = Propagation.SUPPORTS)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void saveTutor(User user) {

//        TutorSignUpRequest tutorSignUpRequest = TestDataBuilder.getTutorSignUpRequest("java,spring");
//        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

        // throw new RuntimeException("RuntimeException");
    }



}
