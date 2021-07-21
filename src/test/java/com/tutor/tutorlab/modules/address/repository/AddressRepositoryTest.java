package com.tutor.tutorlab.modules.address.repository;

import com.tutor.tutorlab.modules.address.vo.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    private final String STATE = "서울특별시";
    private final String SIGUN = "시군";
    private final String GU = "서초구";
    private final String DONG_MYUN_LI = "역삼동";

    @BeforeEach
    void setUp() {
        Address address = Address.builder()
                                 .state(STATE)
                                 .siGun(SIGUN)
                                 .gu(GU)
                                 .dongMyunLi(DONG_MYUN_LI)
                                 .build();

        addressRepository.save(address);
    }

    @Test
    void 주소목록테스트() {
        List<Address> targetStateList = addressRepository.findAllByState(STATE);
        assertThat(targetStateList).hasSize(1);

        List<Address> targetGuList = addressRepository.findAllByStateAndGu(STATE, GU);
        assertThat(targetGuList).hasSize(1);

        List<Address> targetSigunList =addressRepository.findAllByStateAndSiGun(STATE, SIGUN);
        assertThat(targetSigunList).hasSize(1);
    }

}