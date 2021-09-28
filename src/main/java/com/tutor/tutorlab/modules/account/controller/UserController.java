package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
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

import javax.validation.Valid;

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

        Page<UserResponse> users = userService.getUsers(page).map(UserResponse::new);
        return ResponseEntity.ok(users);
    }

    @ApiOperation("회원 조회")
    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "user_id") Long userId) {

        User user = userService.getUser(userId);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @ApiOperation("내정보 조회")
    @GetMapping("/my-info")
    public ResponseEntity<?> getMyInfo(@CurrentUser User user) {

        user = userService.getUser(user.getId());
        return ResponseEntity.ok(new UserResponse(user));
    }

    @ApiOperation("회원 정보 수정")
    @PutMapping
    public ResponseEntity<?> editUser(@CurrentUser User user,
                                      @Valid @RequestBody UserUpdateRequest userUpdateRequest) {

        userService.updateUser(user, userUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<?> quitUser(@CurrentUser User user) {

        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

}
