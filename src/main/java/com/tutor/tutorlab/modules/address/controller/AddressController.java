package com.tutor.tutorlab.modules.address.controller;

import com.tutor.tutorlab.modules.address.controller.request.DongRequest;
import com.tutor.tutorlab.modules.address.controller.request.SiGunGuRequest;
import com.tutor.tutorlab.modules.address.service.AddressService;
import com.tutor.tutorlab.modules.address.util.AddressUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/addresses")
public class AddressController {
    private final AddressService addressService;

    /**
     * 시/도 조회하기
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getStates() throws Exception {
        return addressService.getStates();
    }

    /**
     * 시/군/구 조회하기
     * @param addressRequest
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/siGunGus", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getSiGunGus(@ModelAttribute @Validated SiGunGuRequest addressRequest) throws Exception {
        return addressService.getSiGunGus(addressRequest.getState());
    }

    /**
     * 동 조회하기
     * @param dongRequest
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/dongs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getDongs(@ModelAttribute @Validated DongRequest dongRequest) throws Exception {
        return addressService.getDongs(dongRequest.getState(),
                                       AddressUtils.convertAddress(dongRequest.getSiGun()),
                                       AddressUtils.convertAddress(dongRequest.getGu()));
    }

}
