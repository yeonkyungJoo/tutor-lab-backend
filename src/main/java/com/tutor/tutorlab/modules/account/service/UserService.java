package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.address.util.AddressUtils;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.USER;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService extends AbstractService {

    private final UserRepository userRepository;

    private final TutorService tutorService;
    private final TuteeService tuteeService;

    @Transactional(readOnly = true)
    public Page<User> getUsers(Integer page) {
        return userRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER));
    }

    public void updateUser(User user, UserUpdateRequest userUpdateRequest) {

        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER));

        user.setGender(userUpdateRequest.getGender().equals("MALE") ? GenderType.MALE : GenderType.FEMALE);
        user.setBirth(LocalDateTimeUtil.getStringToDate(userUpdateRequest.getBirth()));
        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        user.setEmail(userUpdateRequest.getEmail());
        user.setNickname(userUpdateRequest.getNickname());
        user.setBio(userUpdateRequest.getBio());
        user.setZone(AddressUtils.convertStringToEmbeddableAddress(userUpdateRequest.getZone()));
        user.setImage(userUpdateRequest.getImage());
    }

    // TODO - Admin인 경우
    public void deleteUser(User user) {

        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER));

        // TODO - check : GrantedAuthority
        if (user.getRole() == RoleType.TUTOR) {
            tutorService.deleteTutor(user);
        }
        tuteeService.deleteTutee(user); // setAuthentication(null)
    }
}
