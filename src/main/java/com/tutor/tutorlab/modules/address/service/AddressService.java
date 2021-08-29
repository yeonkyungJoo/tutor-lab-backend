package com.tutor.tutorlab.modules.address.service;

import com.tutor.tutorlab.modules.address.controller.result.SiGunGuResponse;

import java.util.List;

public interface AddressService {

    List<String> getStates();

    List<SiGunGuResponse> getSiGunGus(String state);

    List<String> getDongs(String state, String siGun, String gu);
}
