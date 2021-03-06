package com.tutor.tutorlab.modules.address.service;

import com.tutor.tutorlab.modules.address.controller.response.SiGunGuResponse;
import com.tutor.tutorlab.modules.address.vo.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {

    List<String> getStates();
    List<Map<String, String>> getStatesMap();

    List<Address> getSiGunGus(String state);
    List<SiGunGuResponse> getSiGunGuResponses(String state);
    List<Map<String, String>> getSigunGusMap(String state);

    List<String> getDongs(String state, String siGunGu);
    List<Map<String, String>> getDongsMap(String state, String siGunGu);

}
