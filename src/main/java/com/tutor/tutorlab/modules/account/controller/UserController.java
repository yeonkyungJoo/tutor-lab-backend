package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.UserService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.notification.repository.NotificationRepository;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
import com.tutor.tutorlab.modules.notification.vo.Notification;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"UserController"})
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController extends AbstractController {

    private final UserService userService;
    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

/*
    @ApiOperation("회원 전체 조회")
    @GetMapping
    public ResponseEntity getUsers() {

        List<UserDto> users = userRepository.findAll().stream()
                .map(user -> new UserDto(user))
                .collect(Collectors.toList());
        return new ResponseEntity(users, HttpStatus.OK);
    }
*/

    // TODO - 검색
    // 페이징
    @ApiOperation("회원 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity getUsers(@RequestParam(defaultValue = "1") Integer page) {

        Page<UserDto> users = userRepository.findByDeleted(false,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(user -> new UserDto(user));
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @ApiOperation("회원 조회")
    @GetMapping("/{user_id}")
    public ResponseEntity getUser(@PathVariable(name = "user_id") Long userId) {

        User user = userRepository.findByDeletedAndId(false, userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        return new ResponseEntity(new UserDto(user), HttpStatus.OK);
    }

    @ApiOperation("회원 정보 수정")
    @PutMapping
    public ResponseEntity editUser(@CurrentUser User user,
                                   @Valid @RequestBody UserUpdateRequest userUpdateRequest) {

        userService.updateUser(user, userUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("회원 탈퇴")
    @DeleteMapping
    public ResponseEntity quitUser(@CurrentUser User user) {

        userService.deleteUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("알림 리스트 - 페이징")
    @GetMapping("/my-notifications")
    public ResponseEntity getNotifications(@CurrentUser User user,
                                           @RequestParam(defaultValue = "1") Integer page) {

        Page<NotificationDto> notifications = notificationRepository.findByUser(user,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(notification -> new NotificationDto(notification));
        return new ResponseEntity(notifications, HttpStatus.OK);
    }

    @ApiOperation("알림 확인")
    @PutMapping("/my-notifications/{notification_id}")
    public ResponseEntity getNotification(@CurrentUser User user,
                                          @PathVariable(name = "notification_id") Long notificationId) {
        Notification notification = notificationService.check(user, notificationId);
        return new ResponseEntity(notification, HttpStatus.OK);
    }

    @ApiOperation("알림 삭제")
    @DeleteMapping("/my-notifications/{notification_id}")
    public ResponseEntity deleteNotification(@CurrentUser User user,
                                             @PathVariable(name = "notification_id") Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // TODO - 알림 전체 삭제 / 선택 삭제
    @ApiOperation("알림 선택 삭제")
    @DeleteMapping("/my-notifications")
    public ResponseEntity deleteNotifications(@CurrentUser User user,
                                              @RequestParam(value = "notification_ids") List<Long> notificationIds) {
        notificationService.deleteNotifications(notificationIds);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    static class NotificationDto {

        public NotificationDto(Notification notification) {
            // this.username = notification.getUser().getUsername();
            this.contents = notification.getContents();
            this.checked = notification.isChecked();
            this.createdAt = LocalDateTimeUtil.getDateTimeToString(notification.getCreatedAt());
            this.checkedAt = LocalDateTimeUtil.getDateTimeToString(notification.getCheckedAt());
        }

        // private String username;    // 수신인
        private String contents;
        private boolean checked;
        private String createdAt;
        private String checkedAt;
    }

    @Data
    static class UserDto {

        private String username;
        // private String password;
        private String name;
        private String gender;
        private String phoneNumber;
        private String email;
        private String nickname;
        private String bio;

        private String zone;

        public UserDto(User user) {
            this.username = user.getUsername();
            this.name = user.getUsername();
            this.gender = user.getGender().toString();
            this.phoneNumber = user.getPhoneNumber();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
            this.bio = user.getBio();
            this.zone = user.getZone();
        }
    }
}
