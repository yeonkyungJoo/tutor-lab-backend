package com.tutor.tutorlab.modules.address.controller;

import com.tutor.tutorlab.modules.address.controller.request.DongRequest;
import com.tutor.tutorlab.modules.address.controller.request.SiGunGuRequest;
import com.tutor.tutorlab.modules.address.controller.response.SiGunGuResponse;
import com.tutor.tutorlab.modules.address.service.AddressService;
import com.tutor.tutorlab.modules.address.util.AddressUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStates() {
        List<String> states = addressService.getStates();
        return ResponseEntity.ok(states);
    }

    // TODO - CHECK : @ModelAttribute
    @GetMapping(value = "/siGunGus", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSiGunGus(@Valid SiGunGuRequest addressRequest) {
        List<SiGunGuResponse> siGunGus = addressService.getSiGunGuResponses(addressRequest.getState());
        return ResponseEntity.ok(siGunGus);
    }

    // TODO - CHECK : @ModelAttribute
    @GetMapping(value = "/dongs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDongs(@Valid DongRequest dongRequest) {
        List<String> dongs = addressService.getDongs(dongRequest.getState(),
                                            AddressUtils.convertAddress(dongRequest.getSiGun()),
                                            AddressUtils.convertAddress(dongRequest.getGu()));
        return ResponseEntity.ok(dongs);
    }

}
