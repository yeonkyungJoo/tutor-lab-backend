package com.tutor.tutorlab.modules.subject.repository;

import com.tutor.tutorlab.modules.subject.vo.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query(nativeQuery = true,
           value = "SELECT DISTINCT parent FROM subject"
    )
    List<String> findParentAll();

    @Query(nativeQuery = true,
           value = "SELECT * FROM subject WHERE parent = :parent"
    )
    List<Subject> findSubjectAllByParent(@Param("parent") String parent);
}
