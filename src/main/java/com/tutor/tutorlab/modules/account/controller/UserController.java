package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.UserService;
import com.tutor.tutorlab.modules.account.vo.User;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final Integer PAGE_SIZE = 20;

    private final UserService userService;
    private final UserRepository userRepository;

/*
    @ApiOperation("회원 전체 조회")
    @GetMapping
    public ResponseEntity getUsers() {

        List<UserDto> users = userRepository.findAll().stream()
                .map(user -> new UserDto(user))
                .collect(Collectors.toList());

        // TODO - RestResponse
        return new ResponseEntity(users, HttpStatus.OK);
    }
*/
    // TODO - 검색
    // 페이징
    @ApiOperation("회원 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity getUsers(@RequestParam(defaultValue = "1") Integer page) {

        Page<UserDto> users = userRepository.findAll(
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(user -> new UserDto(user));
        // TODO - RestResponse
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @ApiOperation("회원 조회")
    @GetMapping("/{user_id}")
    public ResponseEntity getUser(@PathVariable(name = "user_id") Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // TODO - RestResponse
        return new ResponseEntity(new UserDto(user), HttpStatus.OK);
    }

    @ApiOperation("회원 정보 수정")
    @PutMapping
    public ResponseEntity editUser(@CurrentUser User user,
                            @RequestBody UserUpdateRequest userUpdateRequest) {

        if (user == null) {
            // TODO - 예외처리 : UnAuthenticatedException or AccessDeniedException
        }

        userService.updateUser(user, userUpdateRequest);
        // TODO - RestResponse
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("회원 탈퇴")
    @DeleteMapping
    public ResponseEntity quitUser(@CurrentUser User user) {

        if (user == null) {
            // TODO - 예외처리 : UnAuthenticatedException or AccessDeniedException
        }
        userService.deleteUser(user);
        // TODO - RestResponse
        return new ResponseEntity(HttpStatus.OK);
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
