package com.tutor.tutorlab.modules.address.service;

import com.tutor.tutorlab.modules.address.controller.request.DongRequest;
import com.tutor.tutorlab.modules.address.controller.request.SiGunGuRequest;
import com.tutor.tutorlab.modules.address.controller.result.DongResponse;
import com.tutor.tutorlab.modules.address.controller.result.SiGunGuResponse;

import java.util.List;

public interface AddressService {
    List<String> getStates() throws Exception;

    List<SiGunGuResponse> getSiGunGus(String state) throws Exception;

    List<String> getDongs(String state, String siGun, String gu) throws Exception;
}
