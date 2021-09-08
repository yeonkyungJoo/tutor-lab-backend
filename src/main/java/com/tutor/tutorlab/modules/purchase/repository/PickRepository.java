package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PickRepository extends JpaRepository<Pick, Long> {

    List<Pick> findByTutee(Tutee tutee);
    Page<Pick> findByTutee(Tutee tutee, Pageable pageable);

    void deleteAllByTutee(Tutee tutee);
}
