package com.tutor.tutorlab.modules.log.repository;

import com.tutor.tutorlab.modules.log.vo.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface LogRepository extends MongoRepository<Log, String> {
}
