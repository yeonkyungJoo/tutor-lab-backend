package com.tutor.tutorlab.modules.file.repository;

import com.tutor.tutorlab.modules.file.vo.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByUuid(String uuid);
}
