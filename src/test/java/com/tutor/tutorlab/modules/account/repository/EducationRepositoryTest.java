package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.Education;
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
class EducationRepositoryTest {

    @Autowired
    EducationRepository educationRepository;
    @Autowired
    TutorRepository tutorRepository;

    private Tutor tutor;

    @BeforeEach
    void setup() {

        assertNotNull(educationRepository);
        assertNotNull(tutorRepository);

        tutor = tutorRepository.findAll().stream().filter(t -> !t.getEducations().isEmpty()).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Test
    void findByTutorAndId() {

        // given
        assertNotNull(tutor);
        Education education = tutor.getEducations().get(0);
        Long educationId = education.getId();

        // when
        Education result = educationRepository.findByTutorAndId(tutor, educationId)
                .orElseThrow(RuntimeException::new);
        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result).extracting("educationLevel").isEqualTo(education.getEducationLevel()),
                () -> assertThat(result).extracting("schoolName").isEqualTo(education.getSchoolName()),
                () -> assertThat(result).extracting("major").isEqualTo(education.getMajor()),
                () -> assertThat(result).extracting("others").isEqualTo(education.getOthers())
        );
    }

    @Test
    void findByTutor() {

        // given
        assertNotNull(tutor);
        List<Education> educations = tutor.getEducations();

        // when
        List<Education> result = educationRepository.findByTutor(tutor);
        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(educations.size()),
                () -> assertThat(result.get(0)).isEqualTo(educations.get(0))
        );
    }
}