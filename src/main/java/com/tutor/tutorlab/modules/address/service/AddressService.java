package com.tutor.tutorlab.modules.address.service;

import com.tutor.tutorlab.modules.address.controller.response.SiGunGuResponse;
import com.tutor.tutorlab.modules.address.vo.Address;

import java.util.List;

public interface AddressService {

    List<String> getStates();

    List<Address> getSiGunGus(String state);

    List<SiGunGuResponse> getSiGunGuResponses(String state);

    List<String> getDongs(String state, String siGun, String gu);
}
