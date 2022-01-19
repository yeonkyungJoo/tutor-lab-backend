package com.tutor.tutorlab.modules.inquiry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tutor.tutorlab.config.response.Response;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.inquiry.controller.request.InquiryCreateRequest;
import com.tutor.tutorlab.modules.inquiry.service.InquiryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.tutor.tutorlab.config.response.Response.created;

@RequestMapping("/users/my-inquiry")
@RequiredArgsConstructor
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    @ApiOperation("Inquiry 등록")
    @PostMapping
    public ResponseEntity<?> newInquiry(@CurrentUser User user,
                                        @Valid @RequestBody InquiryCreateRequest inquiryCreateRequest) {
        inquiryService.createInquiry(user, inquiryCreateRequest);
        return created();
    }

//    // TODO - TEST : 예외 처리
//    @ApiIgnore
//    @PostMapping("/test-producer")
//    public ResponseEntity<?> test(@RequestBody InquiryCreateRequest inquiryCreateRequest) throws JsonProcessingException {
//        inquiryService.test(inquiryCreateRequest);
//        return ResponseEntity.ok().build();
//    }

}
