package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.response.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.response.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Transactional(readOnly = false)
@RequiredArgsConstructor
@Service
public class CareerService {

    private final CareerRepository careerRepository;
    private final TutorRepository tutorRepository;

    public Career createCareer(User user, CareerCreateRequest careerCreateRequest) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        Career career = Career.builder()
                .tutor(tutor)
                .companyName(careerCreateRequest.getCompanyName())
                .duty(careerCreateRequest.getDuty())
                .startDate(LocalDateTimeUtil.getStringToDate(careerCreateRequest.getStartDate()))
                .endDate(LocalDateTimeUtil.getStringToDate(careerCreateRequest.getEndDate()))
                .present(careerCreateRequest.isPresent())
                .build();
        careerRepository.save(career);
        tutor.addCareer(career);

        return career;
    }

    public void updateCareer(Long careerId, CareerUpdateRequest careerUpdateRequest) {

        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 데이터입니다."));

        career.setCompanyName(careerUpdateRequest.getCompanyName());
        career.setDuty(careerUpdateRequest.getDuty());
        career.setStartDate(LocalDateTimeUtil.getStringToDate(careerUpdateRequest.getStartDate()));
        career.setEndDate(LocalDateTimeUtil.getStringToDate(careerUpdateRequest.getEndDate()));
        career.setPresent(careerUpdateRequest.isPresent());

        career.setUpdatedAt(LocalDateTime.now());
    }

    public void deleteCareer(Long careerId) {

        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 데이터입니다."));

        career.delete();
        careerRepository.delete(career);
    }
}
