package com.tutor.tutorlab.modules.address.controller;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.address.repository.AddressRepository;
import com.tutor.tutorlab.modules.address.vo.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddressControllerTest extends AbstractTest {
    private final String BASE_URL = "/addresses";

    @Autowired
    private AddressRepository addressRepository;

    private final String state = "서울특별시";
    private final String siGun = "";
    private final String gu = "서초구";
    private final String dong = "역삼동";

    @BeforeEach
    void setUp() {
        Address address = Address.builder()
                               .state(state)
                               .siGun(siGun)
                               .gu(gu)
                               .dongMyunLi(dong)
                               .build();

        Address save = addressRepository.save(address);
        System.out.println();
    }

    @Test
    void 시도_by_state() throws Exception {
        mockMvc.perform(get(BASE_URL + "/states")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 시군구조회_by_state() throws Exception {
        mockMvc.perform(get(BASE_URL + "/siGunGus")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("state", state))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 동조회_by_state_siGun_gu() throws Exception {
        mockMvc.perform(get(BASE_URL + "/dongs")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("state", state)
                .param("siGun", siGun)
                .param("gu", gu))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
