package com.tutor.tutorlab.modules.address.mapstruct;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import com.tutor.tutorlab.modules.address.controller.response.AddressResponse;
import com.tutor.tutorlab.modules.address.controller.response.DongResponse;
import com.tutor.tutorlab.modules.address.controller.response.SiGunGuResponse;
import com.tutor.tutorlab.modules.address.vo.Address;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(config = MapstructConfig.class)
public interface AddressMapstruct {

    @Mappings({
//            @Mapping(target = "siGunGu", expression = "java()")
    })
    AddressResponse addressToAddressResponse(Address address);

    @IterableMapping(elementTargetType = AddressResponse.class)
    List<AddressResponse> addressListToAddressResponseList(List<Address> states);

    @Mappings({})
    SiGunGuResponse addressToSiGunGuResponse(Address address);

    @IterableMapping(elementTargetType = SiGunGuResponse.class)
    List<SiGunGuResponse> addressListToSiGunGuResponseList(List<Address> addressList);

    @Mappings({})
    DongResponse addressToDongResponse(Address address);

    @IterableMapping(elementTargetType = DongResponse.class)
    List<DongResponse> addressListToDongResponseList(List<Address> addressList);
}
