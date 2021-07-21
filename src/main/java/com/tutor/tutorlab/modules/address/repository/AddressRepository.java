package com.tutor.tutorlab.modules.address.repository;

import com.tutor.tutorlab.modules.address.vo.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByState(String state);

    List<Address> findAllByStateAndGu(String state, String gu);

    List<Address> findAllByStateAndSiGun(String state, String sigun);
}
