package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.CareerResponse;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
class CareerServiceTest {

    @Mock
    CareerRepository careerRepository;
    @Mock
    TutorRepository tutorRepository;
    @InjectMocks
    CareerService careerService;

    private User user;
    private Tutor tutor;
    private Career career;

    @BeforeEach
    void setup() {

        assertNotNull(careerRepository);
        assertNotNull(tutorRepository);
        assertNotNull(careerService);

        user = Mockito.mock(User.class);
        // tutor = Mockito.mock(Tutor.class);
        tutor = Tutor.of(user);
    }

    @Test
    void getCareerResponse() {

        // given
        when(tutorRepository.findByUser(user)).thenReturn(tutor);

        career = Career.of(tutor, "job", "companyName", "others", "license");
        tutor.addCareer(career);
        when(careerRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(career));

        // when
        CareerResponse careerResponse = careerService.getCareerResponse(user, 1L);
        // then
        assertAll(
                () -> assertThat(careerResponse).extracting("job").isEqualTo(career.getJob()),
                () -> assertThat(careerResponse).extracting("companyName").isEqualTo(career.getCompanyName()),
                () -> assertThat(careerResponse).extracting("others").isEqualTo(career.getOthers()),
                () -> assertThat(careerResponse).extracting("license").isEqualTo(career.getLicense())
        );
    }

    @DisplayName("존재하지 않는 User")
    @Test
    void getCareerResponse_withNotExistUser() {

        // given
        when(tutorRepository.findByUser(user)).thenReturn(null);
        // when
        // then
        assertThrows(UnauthorizedException.class,
                () -> careerService.getCareerResponse(user, 1L));
    }

    @DisplayName("존재하지 않는 CareerId")
    @Test
    void getCareerResponse_withNotExistCareerId() {

        // given
        when(tutorRepository.findByUser(user)).thenReturn(tutor);
        when(careerRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(EntityNotFoundException.class,
                () -> careerService.getCareerResponse(user, 1L));
    }

    @Test
    void createCareer() {

        // given
        when(tutorRepository.findByUser(user)).thenReturn(tutor);
        when(careerRepository.save(any(Career.class))).then(AdditionalAnswers.returnsFirstArg());

        // when
        CareerCreateRequest careerCreateRequest
                = CareerCreateRequest.of("job", "companyName", "others", "license");
        Career response = careerService.createCareer(user, careerCreateRequest);

        // then
        assertThat(response.getTutor()).isEqualTo(tutor);
        assertThat(tutor.getCareers().contains(response)).isTrue();
    }

    @Test
    void createCareer_withNotExistUser() {

        // given
        when(tutorRepository.findByUser(user)).thenReturn(null);
        // when
        // then
        assertThrows(UnauthorizedException.class,
                () -> careerService.createCareer(user, Mockito.mock(CareerCreateRequest.class)));
    }

    @Test
    void updateCareer() {

        // given
        career = Mockito.mock(Career.class);
        when(tutorRepository.findByUser(user)).thenReturn(tutor);
        when(careerRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(career));

        // when
        CareerUpdateRequest careerUpdateRequest
                = CareerUpdateRequest.of("job2", "companyName2", "others2", "license2");
        careerService.updateCareer(user,1L, careerUpdateRequest);

        // then
        verify(career, atMost(0)).setTutor(tutor);
        verify(career, atLeastOnce()).setJob(careerUpdateRequest.getJob());
        verify(career, atLeastOnce()).setCompanyName(careerUpdateRequest.getCompanyName());
        verify(career, atLeastOnce()).setOthers(careerUpdateRequest.getOthers());
        verify(career, atLeastOnce()).setLicense(careerUpdateRequest.getLicense());
    }


    @Test
    void deleteCareer() {

        // given
        when(tutorRepository.findByUser(user)).thenReturn(tutor);

        career = Career.of(tutor, "job", "companyName", "others", "license");
        tutor.addCareer(career);
        when(careerRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(career));

        // when
        careerService.deleteCareer(user, 1L);

        // then
        assertThat(career).extracting("tutor").isNull();
        assertThat(tutor.getCareers().contains(career)).isFalse();
        verify(careerRepository, atLeastOnce()).delete(career);

    }
}