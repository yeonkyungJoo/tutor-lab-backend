package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CareerRepositoryTest {

    @Autowired
    CareerRepository careerRepository;
    @Autowired
    TutorRepository tutorRepository;

    private Tutor tutor;

    @BeforeEach
    void setup() {

        assertNotNull(careerRepository);
        assertNotNull(tutorRepository);

        tutor = tutorRepository.findAll().stream().filter(t -> t.getCareers().size() != 0).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Test
    void findByTutorAndId() {

        // given
        assertNotNull(tutor);
        Career career = tutor.getCareers().get(0);
        Long careerId = career.getId();

        // when
        Career result = careerRepository.findByTutorAndId(tutor, careerId).orElse(null);
        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result).extracting("job").isEqualTo(career.getJob()),
                () -> assertThat(result).extracting("companyName").isEqualTo(career.getCompanyName()),
                () -> assertThat(result).extracting("others").isEqualTo(career.getOthers()),
                () -> assertThat(result).extracting("license").isEqualTo(career.getLicense())
        );
    }

    @Test
    void findByTutor() {

        // given
        assertNotNull(tutor);
        List<Career> careers = tutor.getCareers();

        // when
        List<Career> result = careerRepository.findByTutor(tutor);
        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(careers.size()),
                () -> assertThat(result.get(0)).isEqualTo(careers.get(0))
        );
    }
}