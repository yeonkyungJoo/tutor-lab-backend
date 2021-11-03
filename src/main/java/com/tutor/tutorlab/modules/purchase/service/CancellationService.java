package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.request.CancellationCreateRequest;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;

public interface CancellationService {

    // 수강 취소
    Cancellation cancel(User user, Long lectureId, CancellationCreateRequest cancellationCreateRequest);
}
