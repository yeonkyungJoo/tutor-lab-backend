package com.tutor.tutorlab.test;

import com.tutor.tutorlab.config.init.TestDataBuilder;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MainTestService {

    private final UserRepository userRepository;

    private final IsolationTestService isolationTestService;
    private final PropagationTestService propagationTestService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveTutor() {

        String name = "yk";
        User user = TestDataBuilder.getUser("yk");
        userRepository.save(user);

        try {
            // 둘 다 Propagation.REQUIRED일 때
            // MainTestService.saveTutor()에서 Exception 발생 시 둘 다 rollback
            // PropagationTestService.saveTutor()에서 Exception 발생 시에도 둘 다 rollback
            propagationTestService.saveTutor(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("RuntimeException");

    }
}
