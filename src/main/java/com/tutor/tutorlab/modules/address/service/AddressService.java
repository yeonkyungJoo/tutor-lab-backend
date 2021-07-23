package com.tutor.tutorlab.modules.address.service;

import com.tutor.tutorlab.modules.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;

}
