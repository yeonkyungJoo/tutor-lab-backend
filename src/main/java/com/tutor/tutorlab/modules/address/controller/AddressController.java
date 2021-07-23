package com.tutor.tutorlab.modules.address.controller;

import com.tutor.tutorlab.modules.address.controller.request.AddressRequest;
import com.tutor.tutorlab.modules.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getStates(@ModelAttribute @Validated AddressRequest addressRequest) throws Exception {
//        addressService.getStates();
        return null;
    }

}
