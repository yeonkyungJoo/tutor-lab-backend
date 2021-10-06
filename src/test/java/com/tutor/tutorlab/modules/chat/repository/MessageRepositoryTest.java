package com.tutor.tutorlab.modules.chat.repository;

import com.tutor.tutorlab.modules.chat.vo.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@SpringBootTest
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MongoTemplate mongoTemplate;

//    @Test
//    void findAllByChatroomIdAndCheckedIsFalseAndUsernameIsNot() {
//        System.out.println(messageRepository.countAllByChatroomIdAndCheckedIsFalseAndUsernameIsNot(113L, "user2"));
//    }
//    @Test
//    void query() {
//        List<Message> messages = mongoTemplate.find(Query.query(Criteria.where("chatroomId").is(113L)
//                .and("checked").is(false).and("username").ne("user2")), Message.class);
//        System.out.println(messages.size());
//    }
}