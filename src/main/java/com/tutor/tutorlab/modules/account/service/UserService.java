package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.InvalidInputException;
import com.tutor.tutorlab.modules.account.controller.request.UserQuitRequest;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.UserResponse;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.address.util.AddressUtils;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.notification.repository.NotificationRepository;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.USER;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService extends AbstractService {

    private final UserRepository userRepository;
    private final TutorService tutorService;
    private final TuteeService tuteeService;

    private final NotificationRepository notificationRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private Page<User> getUsers(Integer page) {
        return userRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    public Page<UserResponse> getUserResponses(Integer page) {
        return getUsers(page).map(UserResponse::new);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER));
    }

    public UserResponse getUserResponse(Long userId) {
        return new UserResponse(getUser(userId));
    }

    @Transactional
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
    @Transactional
    public void deleteUser(User user, UserQuitRequest userQuitRequest) {

        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER));

        boolean match = bCryptPasswordEncoder.matches(userQuitRequest.getPassword(), user.getPassword());
        if (!match) {
            throw new InvalidInputException("잘못된 비밀번호입니다.");
        }

        // notification 삭제
        notificationRepository.deleteByUser(user);

        user.setQuitReason(userQuitRequest.getReason());
        // TODO - check : GrantedAuthority
        if (user.getRole() == RoleType.TUTOR) {
            tutorService.deleteTutor(user);
        }
        tuteeService.deleteTutee(user);

        user.quit();
        SecurityContextHolder.getContext().setAuthentication(null);     // 로그아웃
    }
}
