package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.response.Response;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.UserImageUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.request.UserPasswordUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.request.UserQuitRequest;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.UserResponse;
import com.tutor.tutorlab.modules.account.service.UserService;
import com.tutor.tutorlab.modules.account.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.tutor.tutorlab.config.response.Response.ok;

@Api(tags = {"UserController"})
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO - 검색
    @ApiOperation("회원 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "1") Integer page) {

        Page<UserResponse> users = userService.getUserResponses(page);
        return ResponseEntity.ok(users);
    }

    @ApiOperation("회원 조회")
    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "user_id") Long userId) {

        UserResponse user = userService.getUserResponse(userId);
        return ResponseEntity.ok(user);
    }

    @ApiOperation("내정보 조회")
    @GetMapping("/my-info")
    public ResponseEntity<?> getMyInfo(@CurrentUser User user) {
        return ResponseEntity.ok(userService.getUserResponse(user));
    }

    @ApiOperation("회원 정보 수정")
    @PutMapping("/my-info")
    public ResponseEntity<?> editUser(@CurrentUser User user,
                                      @Valid @RequestBody UserUpdateRequest userUpdateRequest) {

        userService.updateUser(user, userUpdateRequest);
        return ok();
    }

    @ApiOperation("프로필 이미지 수정")
    @PutMapping("/my-info/image")
    public ResponseEntity<?> changeImage(@CurrentUser User user,
                                         @RequestBody @Valid UserImageUpdateRequest userImageUpdateRequest) {
        userService.updateUserImage(user, userImageUpdateRequest);
        return ResponseEntity.ok().body(user.getImage());
    }

    @ApiOperation("회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<?> quitUser(@CurrentUser User user,
                                      @RequestBody @Valid UserQuitRequest userQuitRequest) {

        userService.deleteUser(user, userQuitRequest);
        return ok();
    }

    @GetMapping("/quit-reasons")
    public Map<Integer, String> getQuitReasons() {
        return UserQuitRequest.reasons;
    }

    @ApiOperation("비밀번호 변경")
    @PutMapping("/my-password")
    public ResponseEntity<?> changeUserPassword(@CurrentUser User user,
                                                @RequestBody @Valid UserPasswordUpdateRequest userPasswordUpdateRequest) {
        userService.updateUserPassword(user, userPasswordUpdateRequest);
        return ok();
    }

}
