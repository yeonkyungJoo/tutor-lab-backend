package com.tutor.tutorlab.modules.chat.repository;

import com.tutor.tutorlab.modules.chat.vo.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, Long> {
}
