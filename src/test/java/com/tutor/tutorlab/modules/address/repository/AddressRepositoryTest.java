package com.tutor.tutorlab.modules.address.repository;

import com.tutor.tutorlab.modules.address.vo.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    private final String STATE = "서울특별시";
    private final String SIGUN = "";
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

    @Test
    void 시군구조회_by_state() {
        // given
        final String targetState = STATE;

        // when
        List<Address> siGunGuByState = addressRepository.findSiGunGuByState(targetState);

        // then
        Set<String> siGunSet = siGunGuByState.stream()
                                             .map(siGunGu -> siGunGu.getSiGun())
                                             .collect(Collectors.toSet());

        Set<String> guSet = siGunGuByState.stream()
                                          .map(siGunGu -> siGunGu.getGu())
                                          .collect(Collectors.toSet());

        siGunGuByState.forEach(siGunGu -> {
            String state = siGunGu.getState();
            String siGun = siGunGu.getSiGun();
            String gu = siGunGu.getGu();

            assertThat(state).isEqualTo(targetState);
            siGunSet.remove(siGun);
            guSet.remove(gu);
        });

        assertThat(siGunSet).isNullOrEmpty();
        assertThat(guSet).isNullOrEmpty();
    }

    @Test
    void 동조회_by_state_siGun_gu() {
        // given
        final String targetState = STATE;
        final String targetSiGun = SIGUN;
        final String targetGu = GU;

        // when
        List<String> addressList = addressRepository.findDongByStateAndSiGunGu(targetState, targetSiGun, targetGu);

        // then
        System.out.println(addressList.toString());
    }

}