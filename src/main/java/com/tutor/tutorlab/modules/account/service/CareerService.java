package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
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

        }

        Career career = Career.builder()
                .tutor(tutor)
                .companyName(careerCreateRequest.getCompanyName())
                .duty(careerCreateRequest.getDuty())
                .startDate(LocalDate.parse(careerCreateRequest.getStartDate()))
                .endDate(LocalDate.parse(careerCreateRequest.getEndDate()))
                .present(careerCreateRequest.isPresent())
                .build();
        careerRepository.save(career);
        tutor.addCareer(career);

        return career;
    }

    public void updateCareer(Long careerId, CareerUpdateRequest careerUpdateRequest) {

        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 데이터입니다."));

        career.setCompanyName(careerUpdateRequest.getCompanyName());
        career.setDuty(careerUpdateRequest.getDuty());
        career.setStartDate(LocalDate.parse(careerUpdateRequest.getStartDate()));
        career.setEndDate(LocalDate.parse(careerUpdateRequest.getEndDate()));
        career.setPresent(careerUpdateRequest.isPresent());

        career.setUpdatedAt(LocalDateTime.now());
    }

    public void deleteCareer(Long careerId) {

        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 데이터입니다."));

        career.delete();
        careerRepository.delete(career);
    }
}
