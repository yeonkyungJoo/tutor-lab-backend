package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import org.springframework.data.domain.Page;

public interface PickService {

    Page<Pick> getPicks(User user, Integer page);
    Pick createPick(User user, Long lectureId);
    void deletePick(User user, Long pickId);
    void deleteAllPicks(User user);
}
