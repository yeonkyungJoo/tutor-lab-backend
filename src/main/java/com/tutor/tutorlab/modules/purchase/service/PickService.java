package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import org.springframework.data.domain.Page;

public interface PickService {

    Page<Pick> getPicks(User user, Integer page);
    void add(User user, Long lectureId);
    void subtract(User user, Long pickId);
    void clear(User user);
}
