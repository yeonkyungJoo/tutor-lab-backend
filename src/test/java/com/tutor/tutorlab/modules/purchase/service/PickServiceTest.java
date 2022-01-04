package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PickServiceTest {

    @InjectMocks
    PickServiceImpl pickService;
    @Mock
    PickRepository pickRepository;

    @Mock
    TuteeRepository tuteeRepository;
    @Mock
    LectureRepository lectureRepository;

    @Test
    void createPick() {
        // user(tutee), lectureId

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Lecture lecture = Mockito.mock(Lecture.class);
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        // when
        User user = Mockito.mock(User.class);
        pickService.createPick(user, 1L);

        // then
        // pick 생성
        verify(pickRepository).save(Pick.buildPick(tutee, lecture));
    }

    @Test
    void deletePick() {
        // user(tutee), pickId

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Pick pick = Mockito.mock(Pick.class);
        when(pickRepository.findByTuteeAndId(any(Tutee.class), anyLong())).thenReturn(Optional.of(pick));

        // when
        User user = Mockito.mock(User.class);
        pickService.deletePick(user, 1L);

        // then
        verify(pick).delete();
        verify(pickRepository).delete(pick);
    }

    @Test
    void deleteAllPicks() {
        // user(tutee)

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        // when
        User user = Mockito.mock(User.class);
        pickService.deleteAllPicks(user);

        // then
        verify(pickRepository).deleteByTutee(tutee);
    }
}