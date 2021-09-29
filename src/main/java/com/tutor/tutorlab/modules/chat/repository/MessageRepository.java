package com.tutor.tutorlab.modules.chat.repository;

import com.tutor.tutorlab.modules.chat.vo.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface MessageRepository extends MongoRepository<Message, Long> {

    List<Message> findAllByChatroomId(Long chatroomId);
    Message findFirstByChatroomIdOrderByIdDesc(Long chatroomId);

    List<Message> findAllByChatroomIdAndCheckedIsFalseAndUsernameIsNot(Long chatroomId, String username);
    Integer countAllByChatroomIdAndCheckedIsFalseAndUsernameIsNot(Long chatroomId, String username);


}
