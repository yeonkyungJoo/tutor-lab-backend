package com.tutor.tutorlab.modules.chat.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    List<Chatroom> findByTutor(Tutor tutor);
    Page<Chatroom> findByTutor(Tutor tutor, Pageable pageable);
    List<Chatroom> findByTutee(Tutee tutee);
    Page<Chatroom> findByTutee(Tutee tutee, Pageable pageable);

    void deleteByEnrollment(Enrollment enrollment);

}
