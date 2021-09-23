package com.tutor.tutorlab.modules.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {

    @GetMapping("/exception")
    public ResponseEntity<?> getErrorTest() throws Exception {
        boolean errorFlag = true;
        if (Boolean.TRUE.equals(errorFlag)) {
            throw new Exception();
        }
        return ResponseEntity.internalServerError().build();
    }

}
