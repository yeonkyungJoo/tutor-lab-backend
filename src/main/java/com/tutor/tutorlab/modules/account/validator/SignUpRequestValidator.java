package com.tutor.tutorlab.modules.account.validator;

import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class SignUpRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(SignUpRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SignUpRequest request = (SignUpRequest) target;

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            errors.rejectValue("password", "invalid.password", "비밀번호가 일치하지 않습니다.");
        }

        if (errors.hasErrors()) {
        }
    }
}
