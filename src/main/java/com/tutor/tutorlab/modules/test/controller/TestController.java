package com.tutor.tutorlab.modules.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {

//    @GetMapping("/rest")
//    public Object getRestTest() {
//        return "test";
//    }
//
//    @GetMapping("/error")
//    public Object getErrorTest() {
//        boolean errorFlag = true;
//        if (Boolean.TRUE.equals(errorFlag)) {
//            throw new RuntimeException("에러 발생!!");
//        }
//        return "error";
//    }
}
