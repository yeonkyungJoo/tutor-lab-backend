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
    public List<String> getDongs(String state, String SiGunGu) {
        return addressRepository.findDongByStateAndSiGunGu(state,SiGunGu);
    }

    @Override
    public List<Map<String, String>> getMakeSigunGus(List<SiGunGuResponse> siGunGus) {

        List<Map<String, String>> returnList = new ArrayList<>();
        siGunGus.forEach(item->{
            Map<String, String> map = new HashMap<>();
            if (item.getGu().length() > 0 && item.getSiGun().length() > 0){
                map.put("label",item.getSiGun()+" "+item.getGu());
                map.put("value",item.getSiGun()+" "+item.getGu());
            } else if (item.getGu().length() == 0){
                map.put("label",item.getSiGun());
                map.put("value",item.getSiGun());
            } else{
                map.put("label",item.getGu());
                map.put("value",item.getGu());
            }
            returnList.add(map);
        });
        return returnList;
    }
}
