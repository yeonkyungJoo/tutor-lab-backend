package com.tutor.tutorlab.modules.address.service;

import com.tutor.tutorlab.modules.address.controller.response.SiGunGuResponse;
import com.tutor.tutorlab.modules.address.mapstruct.AddressMapstruct;
import com.tutor.tutorlab.modules.address.repository.AddressRepository;
import com.tutor.tutorlab.modules.address.vo.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapstruct addressMapstruct;

    @Override
    public List<String> getStates() {
        return addressRepository.findStates();
    }

    @Override
    public List<Address> getSiGunGus(String state) {
        return addressRepository.findSiGunGuByState(state);
    }

    @Override
    public List<SiGunGuResponse> getSiGunGuResponses(String state) {
        List<Address> list = getSiGunGus(state);
        return addressMapstruct.addressListToSiGunGuResponseList(list);
    }

    @Override
    public List<String> getDongs(String state, String siGun, String gu) {
        return addressRepository.findDongByStateAndSiGunGu(state, siGun, gu);
    }
}
