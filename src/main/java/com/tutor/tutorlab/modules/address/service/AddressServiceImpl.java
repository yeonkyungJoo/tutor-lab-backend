package com.tutor.tutorlab.modules.address.service;

import com.tutor.tutorlab.modules.address.controller.response.SiGunGuResponse;
import com.tutor.tutorlab.modules.address.mapstruct.AddressMapstruct;
import com.tutor.tutorlab.modules.address.repository.AddressRepository;
import com.tutor.tutorlab.modules.address.vo.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final String LABEL = "label";
    private final String VALUE = "value";

    private final AddressRepository addressRepository;
    private final AddressMapstruct addressMapstruct;

    @Override
    public List<String> getStates() {
        return addressRepository.findStates();
    }

    @Override
    public List<Map<String, String>> getStatesMap() {

        List<Map<String, String>> states = new ArrayList<>();
        getStates().forEach(state -> {
            Map<String, String> map = new HashMap<>();
            map.put(LABEL, state);
            map.put(VALUE, state);
            states.add(map);
        });
        return states;
    }

    @Override
    public List<Address> getSiGunGus(String state) {
        return addressRepository.findSiGunGuByState(state);
    }

    @Override
    public List<SiGunGuResponse> getSiGunGuResponses(String state) {
        List<Address> siGunGus = getSiGunGus(state);
        return addressMapstruct.addressListToSiGunGuResponseList(siGunGus);
    }

    @Override
    public List<Map<String, String>> getSigunGusMap(String state) {

        List<Map<String, String>> siGunGus = new ArrayList<>();
        getSiGunGuResponses(state).forEach(siGunGu -> {
            Map<String, String> map = new HashMap<>();
            if (siGunGu.getGu().length() > 0 && siGunGu.getSiGun().length() > 0) {
                map.put(LABEL, siGunGu.getSiGun() + " " + siGunGu.getGu());
                map.put(VALUE, siGunGu.getSiGun() + " " + siGunGu.getGu());
            } else if (siGunGu.getGu().length() == 0) {
                map.put(LABEL, siGunGu.getSiGun());
                map.put(VALUE, siGunGu.getSiGun());
            } else {
                map.put(LABEL, siGunGu.getGu());
                map.put(VALUE, siGunGu.getGu());
            }
            siGunGus.add(map);
        });
        return siGunGus;
    }

    @Override
    public List<String> getDongs(String state, String SiGunGu) {
        return addressRepository.findDongByStateAndSiGunGu(state, SiGunGu);
    }

    @Override
    public List<Map<String, String>> getDongsMap(String state, String siGunGu) {

        List<Map<String, String>> dongs = new ArrayList<>();
        getDongs(state, siGunGu).forEach(dong -> {
            Map<String, String> map = new HashMap<>();
            map.put(LABEL, dong);
            map.put(VALUE, dong);
            dongs.add(map);
        });
        return dongs;
    }

}
