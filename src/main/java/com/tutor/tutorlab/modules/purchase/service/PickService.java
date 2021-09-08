package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.vo.Tutee;

public interface PickService {

    void add(Tutee tutee, Long lectureId);
    void subtract(Tutee tutee, Long pickId);
    void clear(Tutee tutee);
}
