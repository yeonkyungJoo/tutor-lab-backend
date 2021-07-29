package com.tutor.tutorlab.modules.address.mapstruct;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import com.tutor.tutorlab.modules.address.controller.result.AddressResponse;
import com.tutor.tutorlab.modules.address.controller.result.DongResponse;
import com.tutor.tutorlab.modules.address.controller.result.SiGunGuResponse;
import com.tutor.tutorlab.modules.address.vo.Address;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
