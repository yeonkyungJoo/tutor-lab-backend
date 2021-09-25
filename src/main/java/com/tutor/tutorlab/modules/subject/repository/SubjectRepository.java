package com.tutor.tutorlab.modules.subject.repository;

import com.tutor.tutorlab.modules.subject.vo.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query(nativeQuery = true,
           value = "SELECT DISTINCT parent FROM subject"
    )
    List<String> findParent();

    @Query(nativeQuery = true,
           value = "SELECT * FROM subject WHERE parent = :parent"
    )
    List<Subject> findAllByParent(@Param("parent") String parent);
}
