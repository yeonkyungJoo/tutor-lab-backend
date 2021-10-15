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
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.CAREER;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTOR;

@Transactional
@RequiredArgsConstructor
@Service
public class CareerService {

    private final CareerRepository careerRepository;
    private final TutorRepository tutorRepository;

    private Career getCareer(User user, Long careerId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        return careerRepository.findByTutorAndId(tutor, careerId)
                .orElseThrow(() -> new EntityNotFoundException(CAREER));
    }

    @Transactional(readOnly = true)
    public CareerResponse getCareerResponse(User user, Long careerId) {
        return new CareerResponse(getCareer(user, careerId));
    }

    public Career createCareer(User user, CareerCreateRequest careerCreateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Career career = Career.of(
                tutor,
                careerCreateRequest.getCompanyName(),
                careerCreateRequest.getDuty(),
                LocalDateTimeUtil.getStringToDate(careerCreateRequest.getStartDate()),
                LocalDateTimeUtil.getStringToDate(careerCreateRequest.getEndDate()),
                careerCreateRequest.isPresent()
        );
        tutor.addCareer(career);
        return careerRepository.save(career);
    }

    public void updateCareer(User user, Long careerId, CareerUpdateRequest careerUpdateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Career career = careerRepository.findByTutorAndId(tutor, careerId)
                .orElseThrow(() -> new EntityNotFoundException(CAREER));

        career.setCompanyName(careerUpdateRequest.getCompanyName());
        career.setDuty(careerUpdateRequest.getDuty());
        career.setStartDate(LocalDateTimeUtil.getStringToDate(careerUpdateRequest.getStartDate()));
        career.setEndDate(LocalDateTimeUtil.getStringToDate(careerUpdateRequest.getEndDate()));
        career.setPresent(careerUpdateRequest.isPresent());
    }

    public void deleteCareer(User user, Long careerId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Career career = careerRepository.findByTutorAndId(tutor, careerId)
                .orElseThrow(() -> new EntityNotFoundException(CAREER));

        career.delete();
        // TODO - CHECK
        careerRepository.delete(career);
    }
}
