package com.tutor.tutorlab.modules.address.repository;

import com.tutor.tutorlab.modules.address.vo.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByState(String state);

    List<Address> findAllByStateAndGu(String state, String gu);

    List<Address> findAllByStateAndSiGun(String state, String sigun);

    @Query(nativeQuery = true,
           value = "SELECT DISTINCT state FROM address")
    List<String> findStates();

    @Query(nativeQuery = true,
           value = "SELECT * FROM address WHERE state = :state GROUP BY state, si_gun, gu")
    List<Address> findSiGunGuByState(@Param("state") String state);

    @Query(nativeQuery = true,
           value = "SELECT dong_myun_li FROM address GROUP BY state, si_gun, gu, dong_myun_li HAVING state = :state and si_gun = :si_gun and gu = :gu")
    List<String> findDongByStateAndSiGunGu(@Param("state") String state, @Param("si_gun") String siGun, @Param("gu") String gu);
}
